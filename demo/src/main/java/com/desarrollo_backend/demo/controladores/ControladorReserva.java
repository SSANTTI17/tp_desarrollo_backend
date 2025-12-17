package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion; // Importante

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorReserva {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorHuesped gestorHuesped;

    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(
            @RequestParam String tipo,
            @RequestParam String desde,
            @RequestParam String hasta) {
        return gestorReservas.buscarDisponibilidad(tipo, desde, hasta);
    }

    // --- MODIFICACIÓN PRINCIPAL AQUÍ ---
    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody AltaReservaRequest request) {

        // 1. Validaciones básicas de entrada
        if (request.getHabitaciones() == null || request.getHabitaciones().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe seleccionar al menos una habitación"));
        }

        try {
            // 2. BUSCAR AL HUÉSPED REAL (Paso Intermedio Obligatorio)
            // El gestor pide un objeto Huesped, pero el front manda Strings
            // (nombre/apellido)
            HuespedDTO filtro = new HuespedDTO();
            filtro.setApellido(request.getApellido());
            filtro.setNombre(request.getNombre());

            // Usamos tu GestorHuesped para buscarlo
            List<Huesped> clientesEncontrados = gestorHuesped.buscarHuespedes(filtro);

            if (clientesEncontrados.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error",
                        "El cliente " + request.getNombre() + " " + request.getApellido()
                                + " no existe. Regístrelo antes."));
            }

            Huesped huespedReal = clientesEncontrados.get(0);

            // 3. LLAMAR AL GESTOR CON LOS PARAMETROS CORRECTOS
            // Firma: crearReserva(Huesped h, List<Habitacion> habitaciones, String inicio,
            // String fin)
            String resultado = gestorReservas.crearReserva(
                    huespedReal,
                    request.getHabitaciones(),
                    request.getFechaInicio(),
                    request.getFechaFin());

            // 4. MANEJAR LA RESPUESTA (Tu gestor devuelve String, no Reserva)
            if (resultado.startsWith("Error")) {
                return ResponseEntity.badRequest().body(Map.of("error", resultado));
            }

            return ResponseEntity.ok(Map.of("message", resultado));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cancelar-reserva")
    public ResponseEntity<?> eliminarReservas(@RequestBody List<Reserva> reservas) {
        try {
            List<Reserva> listaActualizada = gestorReservas.eliminarReservas(reservas);
            return ResponseEntity.ok(listaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/por-huesped")
    public ResponseEntity<?> buscarPorHuesped(
            @RequestParam String apellido,
            @RequestParam(required = false) String nombre) {
        try {
            List<Reserva> reservas = gestorReservas.consultarReservas(apellido, nombre);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        try {
            // Creamos una dummy solo con el ID para borrar
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

// DTO (Se mantiene igual, asegúrate que esté accesible)
class AltaReservaRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String fechaInicio;
    private String fechaFin;
    // Asumimos que el front manda objetos habitación o al menos sus IDs
    private List<Habitacion> habitaciones;

    // Getters y Setters...
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