package com.desarrollo_backend.demo.facade;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.*;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
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

    public List<Huesped> obtenerHuespedesParaFacturacion(EstadiaDTO estadiaDTO, HabitacionDTO habitacionDTO) {
        // Delegamos al gestor pasándole los datos primitivos necesarios
        Reserva reserva = gestorReservas.consultarReservas(
                habitacionDTO.getNumero(),
                habitacionDTO.getTipo(),
                estadiaDTO.getFechaFin());

        List<Huesped> huespedes = gestorHuespedes.buscarPorReservas(reserva);

        // List<HuespedDTO> dtos = huespedes.stream()
        // .map(huesped -> new HuespedDTO(huesped)) // Usas el constructor que ya tienes
        // en HuespedDTO
        // .collect(Collectors.toList());
        // esto va en el controller, la fachada no debe devolver dtos.
        return huespedes;
    }

    public Factura generarFactura(HuespedDTO huesped, String CUIT, EstadiaDTO estadia, HabitacionDTO habitacion) {
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
        return factura;
    }

    public Factura confirmarFactura(Integer idEstadia, FacturaDTO factura, String cuitResponsable) {

        Estadia estadia = gestorContable.buscarEstadia(idEstadia);

        ResponsablePago responsable = gestorContable.buscarResponsablePorCuit(cuitResponsable); // El CUIT es el ID

        if (responsable == null)
            throw new RuntimeException("Responsable no encontrado");
        Factura facturaReal = new Factura(factura);
        return gestorContable.crearFacturaReal(facturaReal, estadia);
    }

    // METODOS CONSERJE

    // Autentica al conserje verificando usuario y contraseña.
    public boolean autenticarConserje(String usuario, String password) {
        return gestorConserje.autenticar(usuario, password);
    }

    //  Cambia la contraseña del conserje.
    public String cambiarContraseniaConserje(String usuario, String nuevaPass) {
        return gestorConserje.cambiarContrasenia(usuario, nuevaPass);
    }


     // Inicializa un conserje por defecto si la BD está vacía.
    public void inicializarConserje() {
        gestorConserje.crearConserjeInicialSiNoExiste();
    }

}
