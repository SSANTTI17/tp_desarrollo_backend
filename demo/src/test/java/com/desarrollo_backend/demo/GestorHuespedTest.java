package com.desarrollo_backend.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;

@SpringBootTest
public class GestorHuespedTest {
    
    @Autowired
    private GestorHuesped gestorHuesped;

    /**
     * Prueba de Integración: Verifica que la creación de una reserva 
     * no solo pasa la lógica, sino que también guarda correctamente 
     * el registro en la base de datos (H2).
     */
    @Test
    public void testGuardarHuesped_PersistenciaExitosa() {
        // --- ARRANGE (Preparación de datos) ---
        HuespedDTO huesped = new HuespedDTO(new Huesped("Laura", "Gomez", TipoDoc.DNI,
                "87654321", null, "Chile", "laura@test.com", "1122334455", "Hermana", false, "Calle Falsa 123")); // AGREGADO
                                                                                                                  // DIRECCION
        // --- ACT (Ejecución del método a probar) ---
        Huesped huespedCreado = gestorHuesped.darDeAltaHuesped(huesped);
        // --- ASSERT (Verificación de resultados) ---
        assertEquals("Laura", huespedCreado.getNombre(), "El nombre del huesped no coincide");
        assertEquals("Gomez", huespedCreado.getApellido(), "El apellido del huesped no coincide");
        assertEquals(TipoDoc.DNI, huespedCreado.getTipo_documento(), "El tipo de documento del huesped no coincide");
        assertEquals("87654321", huespedCreado.getNroDocumento(), "El número de documento del huesped no coincide");
        assertEquals("Chile", huespedCreado.getNacionalidad(), "La nacionalidad del huesped no coincide");
        assertEquals("laura@test.com", huespedCreado.getEmail(), "El email del huesped no coincide");
        assertEquals("1122334455", huespedCreado.getTelefono());
        assertFalse(huespedCreado.isAlojado());

    }
}
