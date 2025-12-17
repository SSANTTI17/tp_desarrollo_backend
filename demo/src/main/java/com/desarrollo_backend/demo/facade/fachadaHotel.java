package com.desarrollo_backend.demo.facade;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.dtos.ConsumoDTO;
import com.desarrollo_backend.demo.dtos.ContenedorEstadiaYFacturaDTO;
import com.desarrollo_backend.demo.dtos.DireccionDTO;
import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.PersonaJuridicaDTO;
import com.desarrollo_backend.demo.exceptions.EdadInsuficienteException;
import com.desarrollo_backend.demo.gestores.*;
import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaJuridica;
import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
public class FachadaHotel {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorHuesped gestorHuespedes;

    @Autowired
    private GestorContable gestorContable;

    @Autowired
    private GestorHabitaciones gestorHabitaciones;

    @Autowired
    private GestorConserje gestorConserje;

    public String crearReserva(String nombre, String apellido, String telefono,
            List<Habitacion> habitacionesSolicitadas,
            String fechaInicioStr, String fechaFinStr) {
        HuespedDTO huesped = new HuespedDTO();
        huesped.setNombre(nombre);
        huesped.setApellido(apellido);
        huesped.setTelefono(telefono);
        List<Huesped> huespedes = gestorHuespedes.buscarHuespedes(huesped);
        Huesped huespedEntidad = null;
        try {
            huespedEntidad = huespedes.get(0);
        } catch (Exception e) {
            throw new RuntimeException("No existe el huesped");
        }
        return gestorReservas.crearReserva(huespedEntidad, habitacionesSolicitadas,
                fechaInicioStr, fechaFinStr);

    }

    /**
     * Llama al gestor de habitaciones para obtener el estado de todas las
     * habitaciones
     * entre dos fechas dadas.
     * 
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin    Fecha de fin del rango.
     * @return Lista de {@link HabitacionDTO} con el estado de las habitaciones en
     *         el rango especificado.
     */
    public List<HabitacionDTO> consultarEstadoHabitaciones(LocalDate fechaInicio, LocalDate fechaFin) {
        return gestorHabitaciones.mostrarEstadoHabitaciones(fechaInicio, fechaFin);
    }

    /**
     * 
     * 
     * que realiza el check-out en la fecha indicada.
     * Este método es el paso inicial para seleccionar a nombre de quién se rea
     * izará la facturación.
     * Es mejor usar reservas en nuestro caso ya que estadia no tiene huespedes
     * asociados como atributo.
     * 
     * @param estadiaDTO    DTO que contiene la fecha de finalización (chec
     *                      -out) de la estadía.
     * @param habitacionDTO DTO con el número y tipo de habitación a consultar.
     * 
     * 
     * @return Lista de {@link HuespedDTO} con los ocupantes asociados a la reserva.
     * 
     */
    public List<HuespedDTO> obtenerHuespedesParaFacturacion(EstadiaDTO estadiaDTO, HabitacionDTO habitacionDTO) {
        // Delegamos al gestor pasándole los datos primitivos necesarios
        // Las reservas tienen información duplicada de las estadias
        Reserva reserva = gestorReservas.consultarReservas(
                habitacionDTO.getNumero(),
                habitacionDTO.getTipo(),
                estadiaDTO.getFechaFin());

        List<Huesped> huespedes = gestorHuespedes.buscarPorReservas(reserva);

        List<HuespedDTO> dtos = huespedes.stream()
                .map(huesped -> new HuespedDTO(huesped))
                .collect(Collectors.toList());
        return dtos;
    }

    /**
     * 
     * 
     * Verifica que el huésped responsable sea mayor de edad, busca la estadía real
     * en base a los datos
     * de la habitación y fecha, y calcula los montos totales incluyendo consumos.
     * 
     *
     * 
     * @param huesped    DTO del huésped seleccionado como responsable de pago
     *                   (puede ser nulo si es tercero).
     * @param CUIT       Cadena con el CUIT del responsable de pago (si es una
     *                   persona jurídica/tercero).
     * @param estadia    DTO con los datos de fecha de fin para localizar la e
     *                   tadía.
     * @param habitacion DTO con los datos de la habitación para localizar la
     *                   stadía.
     * @return Un ContenedorEstadiaYFacturaDTO que agrupa la estadía encontr
     *         da y la factura generada (no persistida).
     * @throws RuntimeException Si el huésped seleccionado no existe, es meno
     *                          de edad o hay errores en el cálculo.
     */

    public ContenedorEstadiaYFacturaDTO generarFactura(HuespedDTO huesped, String CUIT, EstadiaDTO estadia,
            HabitacionDTO habitacion) throws EdadInsuficienteException {
        Huesped entidad = null;

        if (huesped.getNroDocumento() != null) {
            List<Huesped> entidades = gestorHuespedes.buscarHuespedes(huesped);
            // NUNCA debería entrar acá porque el huésped fue seleccionado antes
            if (entidades.isEmpty()) {
                throw new RuntimeException("No existe el huésped");
            }
            entidad = entidades.get(0);
        }
        Estadia estadiaReal = gestorContable.buscarEstadiaPorCheckout(
                habitacion.getNumero(),
                habitacion.getTipo(),
                estadia.getFechaFin() // Fecha de Check-out
        );

        Factura factura = null;

        factura = gestorContable.generarFacturaParaHuesped(entidad, CUIT, estadiaReal);
        ContenedorEstadiaYFacturaDTO contenedor = new ContenedorEstadiaYFacturaDTO(estadiaReal, factura);
        return contenedor;
    }

    /**
     * 
     * 
     * Este método asocia el responsable de pago definitivo, actualiza el estado de
     * los consumos
     * a "facturados" y guarda la factura en la base de datos vinculándola con la
     * estadía.
     *
     * 
     * @param idEstadia Identificador único de la estadía a facturar.
     * 
     * @param factura   DTO con los datos de la factura a confirmar.
     * @param h         DTO del huésped (usado para buscar responsable si no se
     *                  proveyó una Persona Jurídica).
     * @param resp      DTO de la Persona Jurídica responsable (si aplica).
     * @param consumos  Lista de consumos que se incluyen en esta factura.
     * 
     * @return FacturaDTO confirmado y procesado.
     */
    public FacturaDTO confirmarFactura(Integer idEstadia, FacturaDTO factura, HuespedDTO h, PersonaJuridicaDTO resp,
            List<ConsumoDTO> consumos) {

        Estadia estadia = gestorContable.buscarEstadia(idEstadia);
        Huesped entidad = null;
        ResponsablePago respPago = null;
        // fijarme si cuit != null entonces es nombre de un tercero y busco el
        // responsable pago
        // si cuit == null entonces es el huesped y busco el responsable pago asociado
        // al huesped
        Factura facturaReal = new Factura(factura);
        if (resp != null && resp.getCUIT() != null) {
            respPago = gestorContable.buscarResponsablePorCuit(resp.getCUIT()); // El CUIT es el ID
            facturaReal.setResponsablePago(respPago);
        } else {
            List<Huesped> entidades = gestorHuespedes.buscarHuespedes(h);
            entidad = entidades.get(0);
            facturaReal.setResponsablePago(gestorContable.buscarResponsablePorHuesped(entidad));
        }

        gestorContable.actualizarConsumosEstadia(estadia, consumos);
        gestorContable.crearFacturaReal(facturaReal, estadia);
        return factura;

    }

    public PersonaJuridicaDTO darDeAltaResponsablePago(PersonaJuridicaDTO dto, DireccionDTO direccion) {
        PersonaJuridica responsable = new PersonaJuridica(dto);
        gestorContable.guardarResponsablePago(responsable);
        return dto;
    }

    // METODOS CONSERJE

    // Autentica al conserje verificando usuario y contraseña.
    public boolean autenticarConserje(String usuario, String password) {
        return gestorConserje.autenticar(usuario, password);
    }

    // Cambia la contraseña del conserje.
    public String cambiarContraseniaConserje(String usuario, String nuevaPass) {
        return gestorConserje.cambiarContrasenia(usuario, nuevaPass);
    }

    // Inicializa un conserje por defecto si la BD está vacía.
    public void inicializarConserje() {
        gestorConserje.crearConserjeInicialSiNoExiste();
    }

    /**
     * Método singular: Elimina una sola reserva delegando al Gestor.
     * Este es el método que tu bucle llama internamente.
     */
    public String eliminarReserva(Reserva r) {
        // Delegamos la lógica "dura" al gestor
        // Asumo que tu GestorReserva tiene un método que devuelve un String indicando
        // éxito o error
        try {
            gestorReservas.eliminarReserva(r);
            //
            return "Reserva eliminada con exito";
        } catch (Exception e) {
            return "Error al eliminar: " + e.getMessage();
        }
    }

    /**
     * Metodo iterativo: Procesa una lista y devuelve las que fallaron.
     */
    @Transactional
    public List<Reserva> eliminarReservas(List<Reserva> reservas) {
        List<Reserva> rebotadas = new ArrayList<>();

        for (Reserva r : reservas) {

            if (!eliminarReserva(r).equals("Reserva eliminada con exito")) {
                rebotadas.add(r);
            }
        }
        return rebotadas;
    }

}
