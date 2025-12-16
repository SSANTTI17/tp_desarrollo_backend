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
import com.desarrollo_backend.demo.gestores.*;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaJuridica;
import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;

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

    public List<HabitacionDTO> consultarEstadoHabitaciones(LocalDate fechaInicio, LocalDate fechaFin) {
        return gestorHabitaciones.mostrarEstadoHabitaciones(fechaInicio, fechaFin);
    }

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

    public ContenedorEstadiaYFacturaDTO generarFactura(HuespedDTO huesped, String CUIT, EstadiaDTO estadia,
            HabitacionDTO habitacion) {
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
        try {
            factura = gestorContable.generarFacturaParaHuesped(entidad, CUIT, estadiaReal);
        } catch (Exception e) {
            throw new RuntimeException("El huesped debe ser mayor a 18 años" + e.getMessage());
        }
        ContenedorEstadiaYFacturaDTO contenedor = new ContenedorEstadiaYFacturaDTO(estadiaReal, factura);
        return contenedor;
    }

    public FacturaDTO confirmarFactura(Integer idEstadia, FacturaDTO factura, HuespedDTO h, PersonaJuridicaDTO resp,
            List<ConsumoDTO> consumos) {

        Estadia estadia = gestorContable.buscarEstadia(idEstadia);
        Huesped entidad = null;
        ResponsablePago respPago = null;
        // fijarme si cuit != null entonces es nombre de un tercero y busco el
        // responsable pago
        // si cuit == null entonces es el huesped y busco el responsable pago asociado
        // al huesped
        if (resp != null && resp.getCUIT() != null) {
            respPago = gestorContable.buscarResponsablePorCuit(resp.getCUIT()); // El CUIT es el ID
        } else {
            List<Huesped> entidades = gestorHuespedes.buscarHuespedes(h);
            entidad = entidades.get(0);
        }

        Factura facturaReal = new Factura(factura);
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

}
