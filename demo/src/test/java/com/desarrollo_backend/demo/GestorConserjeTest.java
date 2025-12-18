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

    @Test
    public void testListarTodos_TraeCorrectamente() {
        // GIVEN: Limpiamos y guardamos datos de prueba reales en la BD
        // Nota: Si tienes un data.sql inicial, la base no estará vacía, por eso guardamos y chequeamos existencia.
        conserjeRepo.deleteAll(); 
        
        conserjeRepo.save(new Conserje("adminTest", "123"));
        conserjeRepo.save(new Conserje("userTest", "abc"));

        // WHEN
        List<Conserje> lista = gestorConserje.listarTodos();

        // THEN
        assertEquals(2, lista.size());
        assertTrue(lista.stream().anyMatch(c -> c.getUsuario().equals("adminTest")));
        assertTrue(lista.stream().anyMatch(c -> c.getUsuario().equals("userTest")));
    }

    @Test
    public void testCrearConserje_UsuarioNuevo_SePersisteEnBD() {
        // GIVEN
        String usuario = "nuevo_conserje";
        String pass = "pass_segura";

        // Aseguramos que no exista
        assertFalse(conserjeRepo.findByUsuario(usuario).isPresent());

        // WHEN
        Conserje creado = gestorConserje.crearConserje(usuario, pass);

        // THEN
        assertNotNull(creado);
        assertEquals(usuario, creado.getUsuario());

        // Verificamos directamente en el Repositorio que se haya guardado
        Conserje enBaseDeDatos = conserjeRepo.findByUsuario(usuario).orElse(null);
        assertNotNull(enBaseDeDatos, "El conserje debería estar guardado en la BD");
        assertEquals(pass, enBaseDeDatos.getContrasenia());
    }

    @Test
    public void testCrearConserje_UsuarioDuplicado_LanzaExcepcion() {
        // GIVEN: Creamos un conserje previo
        String usuario = "duplicado";
        conserjeRepo.save(new Conserje(usuario, "passOriginal"));

        // WHEN & THEN: Intentamos crear otro con el mismo usuario
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorConserje.crearConserje(usuario, "otraPass");
        });

        assertEquals("El usuario ya existe", exception.getMessage());
        
        // Verificamos que no se haya duplicado ni modificado el original
        long cantidad = conserjeRepo.findAll().stream()
                .filter(c -> c.getUsuario().equals(usuario))
                .count();
        assertEquals(1, cantidad, "No debería haber duplicados");
    }

}