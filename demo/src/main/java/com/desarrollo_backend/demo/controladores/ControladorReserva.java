package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.desarrollo_backend.demo.gestores.GestorReservas;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
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