package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.dtos.*;
import com.desarrollo_backend.demo.facade.FachadaHotel;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorReserva {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private FachadaHotel fachadaHotel;

    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(
            @RequestParam String tipo,
            @RequestParam String desde,
            @RequestParam String hasta) {
        return gestorReservas.buscarDisponibilidad(tipo, desde, hasta);
    }

    // --- MODIFICACIÓN PRINCIPAL AQUÍ ---
    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody ReservaDTO request) {

        request = fachadaHotel.crearReserva(
                request.getNombre(),
                request.getApellido(),
                request.getTelefono(),
                request.getHabitacionesReservadas(),
                request.getFechaIngreso().toString(),
                request.getFechaEgreso().toString());
        String resultado = (request == null) ? "Error al crear la reserva" : "Reserva creada con éxito";
        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(Map.of("error", resultado));
        } else {
            return ResponseEntity.ok(request);
        }
    }

    @PostMapping("/cancelar-reserva")
    public ResponseEntity<?> eliminarReservas(@RequestBody List<Reserva> reservas) {
        try {
            // Llamamos a la fachada.
            // La fachada devuelve la lista de reservas que FALLARON ("rebotadas").
            List<Reserva> reservasFallidas = fachadaHotel.eliminarReservas(reservas);

            if (reservasFallidas.isEmpty()) {
                // Si la lista está vacía, significa que todas se borraron bien
                return ResponseEntity.ok(Map.of("message", "Todas las reservas fueron eliminadas con éxito."));
            } else {
                // Si devuelve algo, avisamos cuáles no se pudieron borrar
                return ResponseEntity.ok(Map.of(
                        "message", "Algunas reservas no pudieron eliminarse",
                        "fallidas", reservasFallidas));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. BUSCAR POR HUÉSPED
    @GetMapping("/por-huesped")
    public ResponseEntity<?> buscarPorHuesped(
            @RequestParam String apellido,
            @RequestParam(required = false) String nombre) {
        try {
            // Delegamos directo a la fachada
            List<Reserva> reservas = fachadaHotel.buscarPorHuesped(apellido, nombre);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            // Aquí capturamos la ReservaNotFoundException si la fachada la lanza
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3. CANCELAR UNA RESERVA POR ID
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        try {
            // YA NO creamos el objeto dummy aquí.
            // Llamamos al método nuevo de la fachada que recibe el int id.
            String resultado = fachadaHotel.cancelarReserva(id);

            if (resultado.startsWith("Error")) {
                return ResponseEntity.badRequest().body(Map.of("error", resultado));
            }
            return ResponseEntity.ok(Map.of("message", resultado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
