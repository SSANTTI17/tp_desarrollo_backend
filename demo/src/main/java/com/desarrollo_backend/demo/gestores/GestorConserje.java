package com.desarrollo_backend.demo.gestores;

import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import com.desarrollo_backend.demo.repository.ConserjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // <--- Esto lo convierte en un Singleton gestionado por Spring
public class GestorConserje {

    @Autowired
    private ConserjeRepository conserjeRepository; // Usamos el Repo, no el DAO

    // Ya no necesitamos el constructor privado ni el método getInstancia().
    // Spring se encarga de que sea único.

    // --- MÉTODOS DE LÓGICA ---

    /**
     * Verifica si el usuario y contraseña son correctos contra la Base de Datos.
     */
    @Transactional(readOnly = true)
    public boolean autenticar(String nombreIngresado, String passIngresada) {

        // Buscamos en la base de datos por nombre
        Optional<Conserje> conserjeOpt = conserjeRepository.findByNombre(nombreIngresado);

        // Si no existe el usuario
        if (conserjeOpt.isEmpty()) {
            return false;
        }

        Conserje real = conserjeOpt.get();

        // Comparamos la contraseña
        return real.getContrasenia().equals(passIngresada);
    }

    // Cambia la contraseña de un conserje dado su nombre de usuario
    @Transactional
    public String cambiarContrasenia(String nombreUsuario, String nuevaPass) {
        // Primero buscamos al conserje que quiere cambiar la pass
        Optional<Conserje> conserjeOpt = conserjeRepository.findByNombre(nombreUsuario);

        if (conserjeOpt.isPresent()) {
            Conserje conserje = conserjeOpt.get();
            conserje.setContrasenia(nuevaPass);

            // Al usar JPA y @Transactional, el .save() a veces es implícito,
            // pero es buena práctica ponerlo explícito.
            conserjeRepository.save(conserje);
            return "Contraseña actualizada";
        } else {
            return "Error: El conserje no existe";
        }
    }

    // Si no existe el conserje, lo crea
    @Transactional
    public void crearConserjeInicialSiNoExiste() {
        if (conserjeRepository.count() == 0) {
            Conserje admin = new Conserje();
            admin.setNombre("admin");
            admin.setContrasenia("admin123");
            conserjeRepository.save(admin);
        }
    }
}