package com.desarrollo_backend.demo;

import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.gestores.GestorConserje;
import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import com.desarrollo_backend.demo.repository.ConserjeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Transactional

public class GestorConserjeTest {

    @Autowired
    private GestorConserje gestorConserje;

    @Autowired
    private ConserjeRepository conserjeRepo;

    /**
     * Prueba de Integración: Verifica que la creación de una reserva 
     * no solo pasa la lógica, sino que también guarda correctamente 
     * el registro en la base de datos (H2).
     */
    @Test
    public void testAutenticar_Exitoso() {

        //elimino si hay alguno existente
        conserjeRepo.deleteAll();

        String nombre = "admin";
        String pass = "passSuperDificil";

        Conserje conserje = new Conserje(nombre,pass);

        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(nombre, pass);

        assertTrue(pasaTest,"deberia haber autenticado");
    }

    @Test
    public void testAutenticar_PassIncorrecta() {

        //elimino si hay alguno existente
        conserjeRepo.deleteAll();

        String nombre = "admin";
        String pass = "passSuperDificilySegura";

        Conserje conserje = new Conserje(nombre,pass);

        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(nombre, "passFacil");

        assertFalse(pasaTest,"deberia haber tirado error");
    }

    @Test
    public void testAutenticar_PassVacia() {

        //elimino si hay alguno existente
        conserjeRepo.deleteAll();

        String nombre = "admin";
        String pass = "passSuperDificil";

        Conserje conserje = new Conserje(nombre,pass);

        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(nombre, "");

        assertFalse(pasaTest,"deberia haber tirado error");
    }

    @Test
    public void testCambiarContrasenia_Exito() {

        String nombreUsuario = "admin";
        String pass = "passMuyDificilySegura";
        String nuevaPass = "nuevaPassMasDificil";
        
        conserjeRepo.save(new Conserje(nombreUsuario, pass));
        
        String respuesta = gestorConserje.cambiarContrasenia(nombreUsuario, nuevaPass);

        assertEquals(respuesta,"Contraseña actualizada", "falla la salida (la ñ?)");

        //    return "Contraseña actualizada";
        
        //    return "Error: El conserje no existe";
     
    }

    @Test
    public void testCambiarContrasenia_ErrorConserje() {

        String nombreUsuario = "admin2";
        String pass = "passMuyDificilySegura";
        String nuevaPass = "nuevaPassMasDificil";
        
        conserjeRepo.save(new Conserje("admin", pass));
        
        String respuesta = gestorConserje.cambiarContrasenia(nombreUsuario, nuevaPass);

        assertEquals(respuesta,"Error: El conserje no existe", "deberia tirar error");
     
    }

    @Test
    public void testcrearConserjeInicialSiNoExiste_exito(){

        conserjeRepo.deleteAll();

        gestorConserje.crearConserjeInicialSiNoExiste();

        List<Conserje> resultado = conserjeRepo.findAll();
        assertFalse(resultado.isEmpty(), "no se cargó");
        assertEquals(1,resultado.size(),"hay más de uno");
        assertEquals("admin",resultado.get(0).getNombre(),"se guarda mal el nombre");
        assertEquals("admin123",resultado.get(0).getContrasenia(),"se guarda mal la contra");

    } 

}
