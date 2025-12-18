package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(@RequestBody ReservaDTO request) {
        ReservaDTO resultado = fachadaHotel.crearReserva(
                request.getNombre(),
                request.getApellido(),
                request.getTelefono(),
                request.getHabitacionesReservadasEntidad(),
                request.getFechaIngreso(),
                request.getFechaEgreso());

        String mensaje = (resultado == null) ? "Error al crear la reserva" : "Reserva creada con éxito";

        if (mensaje.startsWith("Error")) {
            return ResponseEntity.badRequest().body(Map.of("error", mensaje));
        } else {
            return ResponseEntity.ok(resultado);
        }
    }

    @PostMapping("/cancelar-reserva")
    public ResponseEntity<?> eliminarReservas(@RequestBody List<Reserva> reservas) {
        try {
            List<Reserva> reservasFallidas = fachadaHotel.eliminarReservas(reservas);

            if (reservasFallidas.isEmpty()) {
                return ResponseEntity.ok(Map.of("message", "Todas las reservas fueron eliminadas con éxito."));
            } else {
                return ResponseEntity.ok(Map.of(
                        "message", "Algunas reservas no pudieron eliminarse",
                        "fallidas", reservasFallidas));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/por-huesped")
    public ResponseEntity<?> buscarPorHuesped(
            @RequestParam String apellido,
            @RequestParam(required = false) String nombre) {
        try {
            List<Reserva> reservas = fachadaHotel.buscarPorHuesped(apellido, nombre);

            // Convertimos a DTO para asegurar que el ID viaja y el JSON es plano
            List<ReservaDTO> reservasDTO = reservas.stream()
                    .map(r -> new ReservaDTO(r))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reservasDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        try {
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