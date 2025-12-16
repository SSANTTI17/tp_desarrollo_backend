package com.desarrollo_backend.demo.controladores;

import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import com.desarrollo_backend.demo.repository.ConserjeRepository;
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
    private ConserjeRepository conserjeRepo;

    // Listar (para saber si hay usuarios al inicio)
    @GetMapping
    public List<Conserje> listar() {
        return conserjeRepo.findAll();
    }

    // Crear Nuevo Conserje
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        try {
            String usuario = body.get("usuario");
            String pass = body.get("contrasenia");

            if (usuario == null || pass == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos"));
            }

            if (conserjeRepo.findByUsuario(usuario).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario ya existe"));
            }

            Conserje nuevo = new Conserje(usuario, pass);
            conserjeRepo.save(nuevo);

            return ResponseEntity.ok(Map.of("message", "Conserje creado con éxito"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}