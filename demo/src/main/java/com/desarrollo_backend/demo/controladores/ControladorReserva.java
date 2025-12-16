package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorReserva {

    @Autowired
    private GestorReservas gestorReservas;

    // CU04 - Paso 1: Buscar disponibilidad (Se mantiene igual)
    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(
            @RequestParam String tipo,
            @RequestParam String desde,
            @RequestParam String hasta) {
        return gestorReservas.buscarDisponibilidad(tipo, desde, hasta);
    }

    // --------------------------------------------------------------------------------
    // CU04 - Paso 2: Crear Reserva (MODIFICADO)
    // Ahora recibe una lista de habitaciones y datos del titular en un solo JSON
    // --------------------------------------------------------------------------------
    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody AltaReservaRequest request) {

        // Validamos que vengan datos mínimos
        if (request.getHabitaciones() == null || request.getHabitaciones().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe seleccionar al menos una habitación"));
        }

        // Llamamos al Gestor pasando los datos desglosados
        String resultado = gestorReservas.crearReserva(
                request.getNombre(),
                request.getApellido(),
                request.getTelefono(),
                request.getHabitaciones(),
                request.getFechaInicio(),
                request.getFechaFin());

        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(Map.of("error", resultado));
        }
        return ResponseEntity.ok(Map.of("message", resultado));
    }

    // CU06 Cancelar Reserva (MULTIPLES) - NUEVO
    @PostMapping("/cancelar-reserva")
    public ResponseEntity<?> eliminarReservas(@RequestBody List<Reserva> reservas) {
        try {
            // Llamamos al gestor que procesará la lista y devolverá la lista actualizada
            List<Reserva> listaActualizada = gestorReservas.eliminarReservas(reservas);

            return ResponseEntity.ok(listaActualizada);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // - Buscar reservas por huésped (Se mantiene igual)
    @GetMapping("/por-huesped")
    public ResponseEntity<?> buscarPorHuesped(
            @RequestParam String apellido,
            @RequestParam(required = false) String nombre) {
        try {
            HuespedDTO filtro = new HuespedDTO();
            filtro.setApellido(apellido);
            filtro.setNombre(nombre);
            List<Reserva> reservas = gestorReservas.consultarReservas(filtro);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // CU06 - Cancelar reserva individual (Se mantiene igual por compatibilidad)
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        try {
            Reserva reservaDummy = new Reserva();
            reservaDummy.setId(id);

            String resultado = gestorReservas.eliminarReserva(reservaDummy);

            if (resultado.startsWith("Error")) {
                return ResponseEntity.badRequest().body(Map.of("error", resultado));
            }
            return ResponseEntity.ok(Map.of("message", resultado));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

// --------------------------------------------------------------------------------
// DTO AUXILIAR PARA RECIBIR EL JSON COMPLETO DEL FRONTEND
// (Puedes ponerlo en un archivo separado si prefieres: AltaReservaRequest.java)
// --------------------------------------------------------------------------------
class AltaReservaRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String fechaInicio;
    private String fechaFin;
    private List<Habitacion> habitaciones;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }
}