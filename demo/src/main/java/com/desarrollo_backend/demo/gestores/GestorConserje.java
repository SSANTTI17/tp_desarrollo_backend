package com.desarrollo_backend.demo.gestores;

import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import com.desarrollo_backend.demo.repository.ConserjeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GestorConserje {

    @Autowired
    private ConserjeRepository conserjeRepository;

    public List<Conserje> listarTodos() {
        return conserjeRepository.findAll();
    }

    @Transactional
    public Conserje crearConserje(String usuario, String contrasenia) throws IllegalArgumentException {
        // Validar si ya existe
        if (conserjeRepository.findByUsuario(usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Conserje nuevo = new Conserje(usuario, contrasenia);
        return conserjeRepository.save(nuevo);
    }

    //BORRAR    

    /**
     * Verifica si el usuario y contraseña son correctos contra la Base de Datos.
     */
    @Transactional(readOnly = true)
    public boolean autenticar(String usuarioIngresado, String passIngresada) {

        // Buscamos por 'usuario' (el login)
        Optional<Conserje> conserjeOpt = conserjeRepository.findByUsuario(usuarioIngresado);

        if (conserjeOpt.isEmpty()) {
            return false;
        }

        Conserje real = conserjeOpt.get();
        return real.getContrasenia().equals(passIngresada);
    }

    // Cambia la contraseña dado el usuario
    @Transactional
    public String cambiarContrasenia(String usuario, String nuevaPass) {
        Optional<Conserje> conserjeOpt = conserjeRepository.findByUsuario(usuario);

        if (conserjeOpt.isPresent()) {
            Conserje conserje = conserjeOpt.get();
            conserje.setContrasenia(nuevaPass);
            conserjeRepository.save(conserje);
            return "Contraseña actualizada";
        } else {
            return "Error: El conserje no existe";
        }
    }

    // Bootstrapping: Si no hay nadie, crea el admin por defecto
    @Transactional
    public void crearConserjeInicialSiNoExiste() {
        if (conserjeRepository.count() == 0) {
            Conserje admin = new Conserje();
            admin.setUsuario("admin");
            admin.setContrasenia("admin");
            conserjeRepository.save(admin);
        }
    }
}