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

    // CU04 - Paso 1: Buscar disponibilidad
    @GetMapping("/buscar")
    public List<Map<String, Object>> buscar(
            @RequestParam String tipo,
            @RequestParam String desde,
            @RequestParam String hasta) {
        return gestorReservas.buscarDisponibilidad(tipo, desde, hasta);
    }

    // CU04 - Paso 2: Crear Reserva (DESCOMENTADO Y CORREGIDO)
    @PostMapping("/crear")
    public ResponseEntity<?> crearReserva(
            @RequestParam String tipo,
            @RequestParam int numero,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestBody(required = false) HuespedDTO titular // Recibimos el titular en el cuerpo
    ) {
        // Si el front no manda titular, creamos uno vacío para evitar
        // NullPointerException
        if (titular == null) {
            titular = new HuespedDTO();
        }

        String resultado = gestorReservas.crearReserva(titular, tipo, numero, fechaInicio, fechaFin);

        if (resultado.startsWith("Error")) {
            return ResponseEntity.badRequest().body(Map.of("error", resultado));
        }
        return ResponseEntity.ok(Map.of("message", resultado));
    }

    // CU06 - Buscar reservas por huésped
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

    // CU06 - Cancelar reserva
    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int id) {
        try {
            Reserva reservaDummy = new Reserva();
            reservaDummy.setId(id); // Esto ahora funciona gracias a tu corrección en el Modelo

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