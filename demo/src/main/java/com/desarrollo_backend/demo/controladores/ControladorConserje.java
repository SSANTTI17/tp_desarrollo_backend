package com.desarrollo_backend.demo.controladores;

import com.desarrollo_backend.demo.facade.FachadaHotel;
import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conserjes")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorConserje {

    @Autowired
    private FachadaHotel fachadaHotel; // <--- Comunicación con la Fachada

    // Listar
    @GetMapping
    public ResponseEntity<List<Conserje>> listar() {
        List<Conserje> lista = fachadaHotel.listarConserjes();
        return ResponseEntity.ok(lista);
    }

    // Crear Nuevo Conserje
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        try {
            String usuario = body.get("usuario");
            String pass = body.get("contrasenia");

            if (usuario == null || pass == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos (usuario o contrasenia)"));
            }

            // Delegamos la operación a la fachada
            fachadaHotel.registrarNuevoConserje(usuario, pass);

            return ResponseEntity.ok(Map.of("message", "Conserje creado con éxito"));

        } catch (IllegalArgumentException e) {
            // Capturamos validaciones de negocio (ej: usuario duplicado)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
}