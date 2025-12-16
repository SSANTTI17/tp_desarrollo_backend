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

    @Test
    public void testAutenticar_Exitoso() {
        conserjeRepo.deleteAll();
        String usuario = "admin";
        String pass = "passSuperDificil";

        // Usamos el constructor nuevo (usuario, pass)
        Conserje conserje = new Conserje(usuario, pass);
        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(usuario, pass);
        assertTrue(pasaTest, "Deberia haber autenticado correctamente");
    }

    @Test
    public void testAutenticar_PassIncorrecta() {
        conserjeRepo.deleteAll();
        String usuario = "admin";
        String pass = "passSuperDificilySegura";

        Conserje conserje = new Conserje(usuario, pass);
        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(usuario, "passFacil");
        assertFalse(pasaTest, "Deberia haber fallado la autenticación");
    }

    @Test
    public void testAutenticar_PassVacia() {
        conserjeRepo.deleteAll();
        String usuario = "admin";
        String pass = "passSuperDificil";

        Conserje conserje = new Conserje(usuario, pass);
        conserjeRepo.save(conserje);

        boolean pasaTest = gestorConserje.autenticar(usuario, "");
        assertFalse(pasaTest, "Deberia haber fallado con contraseña vacía");
    }

    @Test
    public void testCambiarContrasenia_Exito() {
        String usuario = "admin";
        String pass = "passMuyDificilySegura";
        String nuevaPass = "nuevaPassMasDificil";

        conserjeRepo.save(new Conserje(usuario, pass));

        String respuesta = gestorConserje.cambiarContrasenia(usuario, nuevaPass);
        assertEquals("Contraseña actualizada", respuesta);
    }

    @Test
    public void testCambiarContrasenia_ErrorConserje() {
        String usuario = "admin2";
        String pass = "passMuyDificilySegura";
        String nuevaPass = "nuevaPassMasDificil";

        conserjeRepo.save(new Conserje("admin", pass));

        String respuesta = gestorConserje.cambiarContrasenia(usuario, nuevaPass);
        assertEquals("Error: El conserje no existe", respuesta);
    }

    @Test
    public void testcrearConserjeInicialSiNoExiste_exito() {
        conserjeRepo.deleteAll();

        gestorConserje.crearConserjeInicialSiNoExiste();

        List<Conserje> resultado = conserjeRepo.findAll();
        assertFalse(resultado.isEmpty(), "No se cargó el conserje inicial");
        assertEquals(1, resultado.size(), "Debería haber exactamente 1 conserje");

        // CORRECCIÓN CLAVE: Usamos getUsuario() en lugar de getNombre()
        assertEquals("admin", resultado.get(0).getUsuario(), "El usuario admin no se guardó correctamente");
        assertEquals("admin", resultado.get(0).getContrasenia(), "La contraseña admin no se guardó correctamente");
    }
}