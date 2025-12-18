package com.desarrollo_backend.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.facade.FachadaHotel;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorHabitacion {

    @Autowired
    private FachadaHotel fachadaHotel;

    @GetMapping("/estado")
    public List<HabitacionDTO> getEstadoHabitaciones(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        // El controlador recibe la petición Web y llama a la Fachada
        return fachadaHotel.getEstadoHabitaciones(desde, hasta);
    }



}