package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorReserva {

    @Autowired
    private GestorReservas gestorReservas;

    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(
            @RequestParam String tipo,
            @RequestParam String desde,
            @RequestParam String hasta) {
        return gestorReservas.buscarDisponibilidad(tipo, desde, hasta);
    }

    // 1. Buscar reservas de un huésped (para mostrarlas en la tabla de cancelar)
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

    // 2. Cancelar una reserva
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

    /*
    VER
    @PostMapping("/crear")
    public String crearReserva(
            @RequestParam String tipo,
            @RequestParam int numero,
            @RequestParam String fechaInicio, // yyyy-MM-dd
            @RequestParam String fechaFin // yyyy-MM-dd
    ) {
        return gestorReservas.crearReserva(tipo, numero, fechaInicio, fechaFin);
    }*/

}