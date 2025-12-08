package com.desarrollo_backend.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.desarrollo_backend.demo.gestores.GestorHabitaciones;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.OcuparDTO;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
public class ControladorHabitacion {

    @Autowired
    private GestorHabitaciones gestorHabitaciones;

    @GetMapping("/estado")
    public List<HabitacionDTO> getEstadoHabitaciones(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return gestorHabitaciones.mostrarEstadoHabitaciones(desde, hasta);
    }

    @PostMapping("/ocupar")
    public ResponseEntity<?> ocuparHabitacion(@RequestBody OcuparDTO ocupacionDTO) {
        try {
            gestorHabitaciones.registrarOcupacion(ocupacionDTO);
            return ResponseEntity.ok("{\"mensaje\": \"Ocupación registrada con éxito\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    
}