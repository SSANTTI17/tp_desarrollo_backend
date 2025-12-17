package com.desarrollo_backend.demo.gestores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Date;
import com.desarrollo_backend.demo.modelo.responsablePago.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.repository.PersonaFisicaRepository;
import com.desarrollo_backend.demo.repository.PersonaJuridicaRepository;
import com.desarrollo_backend.demo.repository.ResponsablePagoRepository;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.factura.TipoFactura;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.repository.EstadiaRepository;
import com.desarrollo_backend.demo.repository.FacturaRepository;
import com.desarrollo_backend.demo.dtos.*;
import java.util.List;

import jakarta.transaction.Transactional;

@Service
public class GestorContable {

    @Autowired
    private PersonaFisicaRepository personaFisicaRepository;
    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;
    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;
    @Autowired
    private GestorHuesped gestorHuesped;
    @Autowired
    private EstadiaRepository estadiaRepository;
    @Autowired
    private FacturaRepository FacturaRepository;


    public ResponsablePago registrarResponsable(ResponsablePago responsable) {

        ResponsablePago respp;
        if (responsable instanceof PersonaFisica fisica)
            respp = personaFisicaRepository.save(fisica);
        else if (responsable instanceof PersonaJuridica juridica)
            respp = personaJuridicaRepository.save(juridica);
        else
            return null;

        responsablePagoRepository.save(new ResponsablePago(respp.getCUIT()));

        return respp;
    }

    public void registrarPersonaFisica(PersonaFisicaDTO pfDto, Huesped huespedReferencia) {
        if (pfDto == null || pfDto.getCUIT() == null)
            return;

        String cuitLimpio = pfDto.getCUIT().replaceAll("[^0-9]", "");
        String cuitFormateado = cuitLimpio;
        if (cuitLimpio.length() == 11) {
            cuitFormateado = cuitLimpio.substring(0, 2) + "-" +
                    cuitLimpio.substring(2, 10) + "-" +
                    cuitLimpio.substring(10, 11);
        }

        PersonaFisica nuevaPF = new PersonaFisica(
                pfDto.getPosicionIVA(),
                cuitFormateado,
                huespedReferencia);

        personaFisicaRepository.save(nuevaPF);
    }

    public void modificarHuesped(HuespedDTO dto){
        
        // Recuperamos el Huésped
        HuespedPK idHuesped = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        Huesped huesped = gestorHuesped.obtenerHuespedPorId(idHuesped);

        // Buscamos si ya tiene una PersonaFisica asignada
        PersonaFisica pfExistente = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);

        String nuevoCuit = dto.getCUIT();
        String nuevaPosicionIVA = dto.getPosicionIVA(); 

        // Validamos si viene un CUIT real (no nulo y no vacío)
        boolean tieneCuitNuevo = nuevoCuit != null && !nuevoCuit.trim().isEmpty();

        if (pfExistente == null) {
            // CASO A: No tenía datos previos.
            // Solo creamos si ahora SÍ trae CUIT.
            if (tieneCuitNuevo) {
                PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
                personaFisicaRepository.save(nuevaPF);
            }
        
        } else {
            // CASO B: Ya tenía datos fiscales.
            if (!tieneCuitNuevo) {
                // Si ahora el usuario borró el CUIT, eliminamos el registro fiscal existente.
                personaFisicaRepository.delete(pfExistente);
            } else {
                // Trae CUIT. Verificamos si cambió.
                if (!pfExistente.getCUIT().equals(nuevoCuit)) {
                    // Cambió el CUIT (que es el ID). Debemos borrar y crear de nuevo.
                    personaFisicaRepository.delete(pfExistente);
                    personaFisicaRepository.flush(); // Forzamos el borrado inmediato

                    PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
                    personaFisicaRepository.save(nuevaPF);

                } else {
                    // El CUIT es el mismo. Solo actualizamos el IVA.
                    pfExistente.setPosicionIVA(nuevaPosicionIVA);
                    personaFisicaRepository.save(pfExistente);
                }
            }
        }
    }
    /**
     * Realiza la lógica de negocio para la confección de una factura preliminar.
     * Valida que el huésped sea mayor de 18 años, determina el responsable de pago (Huésped o Tercero/Empresa),
     * calcula los totales sumando el costo de la estadía y los consumos pendientes, y define el
     * tipo de factura (A o B) según la condición fiscal del responsable.
     *
     * @param huesped Entidad del huésped seleccionado (puede ser null si paga un tercero).
     * @param CUIT    CUIT del responsable de pago externo (usado si huesped es null).
     * @param estadia La estadía sobre la cual se está facturando.
     * @return Una entidad {@link Factura} con los cálculos realizados y el responsable asignado (sin persistir).
     * @throws Exception Si el huésped seleccionado es menor de edad.
     */
    public Factura generarFacturaParaHuesped(Huesped huesped, String CUIT, Estadia estadia) throws Exception {

        // VALIDACIÓN: MENOR DE EDAD
        if (huesped != null) {
            if (huesped.calcularEdad() < 18) {
                throw new Exception("Error: El huésped seleccionado es menor de edad y no puede ser responsable de pago.");
            }
        }

        // 2. OBTENER RESPONSABLE DE PAGO
        ResponsablePago responsable = null;

        if (huesped != null) {
            responsable = this.buscarResponsablePorHuesped(huesped);
        } else if (CUIT != null && !CUIT.isEmpty()) {
            responsable = this.buscarResponsablePorCuit(CUIT);
        }

        // No se encontró responsable (Huesped es null y Cuit es null)
        if (responsable == null) {
            return null; // Retornar NULL indicará al Controller que debe redirigir a "Alta Responsable"
        }

        float valorEstadia = estadia.getPrecio(); 
        // Consumos
        float totalConsumos = estadia.totalConsumos(); 
        float totalAPagar = valorEstadia + totalConsumos;

        // 5. DETERMINAR TIPO FACTURA (A o B)
        TipoFactura tipoFactura;
        if(responsable instanceof PersonaFisica){
            tipoFactura = TipoFactura.A; 
        }else{
            tipoFactura = TipoFactura.B; // (Consumidor Final)
        }

        // 6. ARMAR EL OBJETO FACTURA
        Factura factura = new Factura();
        factura.setResponsablePago(responsable);
        factura.setTipoFactura(tipoFactura);
        factura.setValorEstadia(valorEstadia);
        factura.setTotalAPagar(totalAPagar);
        factura.setPagado(false); // Aún no se paga
        factura.setVuelto(0);

        estadia.setFactura(factura);
        return factura;
    }
    /**
     * Busca un responsable de pago existente en la base de datos utilizando su CUIT como identificador.
     *
     * @param cuit El CUIT del responsable a buscar.
     * @return La entidad {@link ResponsablePago} si existe, o {@code null} en caso contrario.
     */
    public ResponsablePago buscarResponsablePorCuit(String cuit) {
        return responsablePagoRepository.findById(cuit).orElse(null);
    }

    public ResponsablePago buscarResponsablePorHuesped(Huesped huesped) {
        return personaFisicaRepository.findByRefHuesped(huesped).orElse(null);
    }
    /**
     * Busca una estadía activa que coincida exactamente con el número de habitación,
     * tipo de habitación y que tenga fecha de salida (check-out) en el día indicado.
     *
     * @param numero   Número de la habitación.
     * @param tipo     Tipo de habitación (enum).
     * @param fechaFin Fecha de check-out esperada.
     * @return La entidad Estadia encontrada, o {@code null} si no existe coincidencia.
     */
    public Estadia buscarEstadiaPorCheckout(int numero, TipoHabitacion tipo, Date fechaFin) {
        return estadiaRepository.buscarPorHabitacionYFechaFin(numero, tipo, fechaFin)
                .orElse(null);
    }
    /**
     * Busca una estadía por su identificador único.
     * * @param idEstadia ID de la estadía.
     * @return La entidad {@link Estadia}.
     * @throws RuntimeException Si no se encuentra la estadía con el ID proporcionado.
     */
    public Estadia buscarEstadia(int idEstadia) {
        return estadiaRepository.findById(idEstadia)
            .orElseThrow(() -> new RuntimeException("Estadía no encontrada"));
    }
    /**
     * Persiste la factura definitiva en la base de datos y establece la relación bidireccional
     * con la estadía correspondiente.
     *
     * @param factura La entidad Factura a guardar.
     * @param estadia La estadía a la cual pertenece dicha factura.
     */
    @Transactional
    public void crearFacturaReal(Factura factura, Estadia estadia) {
        
        estadia.setFactura(factura);
        estadiaRepository.save(estadia);
        FacturaRepository.save(factura);
    }

    public void guardarResponsablePago(ResponsablePago responsable) {
        responsablePagoRepository.save(responsable);
    }
    /**
     * Actualiza el estado de los ítems de consumo asociados a una estadía.
     * Marca como "facturado" (true) aquellos consumos que coinciden con la lista de DTOs recibida,
     * para evitar que sean cobrados nuevamente en el futuro.
     *
     * @param estadia     La entidad de la estadía cuyos consumos se actualizarán.
     * @param consumosDTO Lista de DTOs que representan los consumos que acaban de ser facturados.
     */
    @Transactional
    public void actualizarConsumosEstadia(Estadia estadia, List<ConsumoDTO> consumosDTO) {

        for (ConsumoDTO dto : consumosDTO) {
        estadia.getConsumos().stream()
            .filter(consumo -> consumo.getId() == dto.getId()) 
            .findFirst()
            .ifPresent(consumoEncontrado -> {
                // Actualizamos el estado a facturado
                consumoEncontrado.setFacturado(true);
            });
    }
        estadiaRepository.save(estadia);
    }
}



