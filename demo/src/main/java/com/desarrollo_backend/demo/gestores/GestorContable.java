package com.desarrollo_backend.demo.gestores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.PersonaFisicaDTO;
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
        
        // 1. Recuperamos el Huésped (necesario para vincular la PersonaFisica)
        HuespedPK idHuesped = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        Huesped huesped = gestorHuesped.obtenerHuespedPorId(idHuesped);

        // 2. Buscamos si ya tiene una PersonaFisica asignada
        PersonaFisica pfExistente = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);

        String nuevoCuit = dto.getCUIT();
        String nuevaPosicionIVA = dto.getPosicionIVA(); 

        if (pfExistente == null) {
            // CASO A: El huésped no tenía datos fiscales previos. Creamos uno nuevo.
            PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
            personaFisicaRepository.save(nuevaPF);
        
        } else {
            // CASO B: Ya tiene datos. Verificamos si cambió el CUIT.
            if (!pfExistente.getCUIT().equals(nuevoCuit)) {
                
                // Como el CUIT es ID, no podemos hacer setCUIT. 
                // Debemos borrar el registro viejo y crear uno nuevo.
                personaFisicaRepository.delete(pfExistente);
                
                // Forzamos el flush para que el DELETE se ejecute en la BD antes del INSERT
                // Esto evita errores de clave duplicada si hubiera conflictos raros
                personaFisicaRepository.flush(); 

                PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
                personaFisicaRepository.save(nuevaPF);

            } else {
                // --- EL CUIT ES EL MISMO (Simple) ---
                // Solo actualizamos la posición del IVA
                pfExistente.setPosicionIVA(nuevaPosicionIVA);
                personaFisicaRepository.save(pfExistente);
            }
        }

    }

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

    public ResponsablePago buscarResponsablePorCuit(String cuit) {
        return responsablePagoRepository.findById(cuit).orElse(null);
    }

    public ResponsablePago buscarResponsablePorHuesped(Huesped huesped) {
        return personaFisicaRepository.findByRefHuesped(huesped).orElse(null);
    }

    public Estadia buscarEstadiaPorCheckout(int numero, TipoHabitacion tipo, Date fechaFin) {
        return estadiaRepository.buscarPorHabitacionYFechaFin(numero, tipo, fechaFin)
                .orElse(null);
    }

    public Estadia buscarEstadia(int idEstadia) {
        return estadiaRepository.findById(idEstadia)
            .orElseThrow(() -> new RuntimeException("Estadía no encontrada"));
    }

    @Transactional
    public Factura crearFacturaReal(Factura factura, Estadia estadia) {
        
        estadia.setFactura(factura);
        estadiaRepository.save(estadia);
        return FacturaRepository.save(factura);
    }
}



