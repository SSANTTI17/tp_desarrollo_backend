package com.desarrollo_backend.demo.controladores;

import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import com.desarrollo_backend.demo.repository.ConserjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorAuth {

    @Autowired
    private ConserjeRepository conserjeRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String user = credenciales.get("usuario");
        String pass = credenciales.get("clave");

        Optional<Conserje> conserjeOpt = conserjeRepo.findByUsuario(user);

        if (conserjeOpt.isPresent()) {
            Conserje conserje = conserjeOpt.get();
            if (conserje.getContrasenia().equals(pass)) {
                return ResponseEntity.ok(Map.of(
                        "token", "token-acceso-simulado",
                        "usuario", Map.of(
                                "id", conserje.getId(),
                                "nombre", conserje.getUsuario(),
                                "rol", "CONSERJE")));
            }
        }

        // --- CAMBIO AQUÍ: Mensaje más humano ---
        return ResponseEntity.status(401).body(Map.of(
                "error", "El usuario o la contraseña son incorrectos. Por favor, verificá tus datos."));
    }
}