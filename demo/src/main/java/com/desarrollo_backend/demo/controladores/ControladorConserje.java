package com.desarrollo_backend.demo.controladores;

import com.desarrollo_backend.demo.facade.FachadaHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api/conserje")
public class ControladorConserje {

    @Autowired
    private FachadaHotel fachada; // <--- Usamos la Fachada

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        // Llamamos a la fachada
        boolean valido = fachada.autenticarConserje(
                credenciales.get("usuario"),
                credenciales.get("password"));
        if (valido)
            return ResponseEntity.ok("Login exitoso");
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}