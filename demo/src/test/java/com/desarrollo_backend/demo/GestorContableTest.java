package com.desarrollo_backend.demo;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.PersonaFisicaDTO;
import com.desarrollo_backend.demo.gestores.GestorContable;
import com.desarrollo_backend.demo.modelo.estadias.Consumo;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.estadias.Moneda;
import com.desarrollo_backend.demo.modelo.estadias.TipoConsumo;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.factura.TipoFactura;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;
import com.desarrollo_backend.demo.repository.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GestorContableTest {

    @Autowired
    private GestorContable gestorContable;
    @Autowired
    private HuespedRepository huespedRepository;
    @Autowired
    private PersonaFisicaRepository personaFisicaRepository;
    @Autowired
    private EstadiaRepository estadiaRepository;
    @Autowired
    private HabitacionRepository habitacionRepository;

    @Test
    public void testRegistrarPersonaFisica() {
        // GIVEN: Huesped previo
        Huesped huesped = new Huesped();
        huesped.setNombre("Test User");
        huesped.setDocumento(TipoDoc.DNI, "12345678");
        huespedRepository.save(huesped);

        // --- CORRECCIÓN AQUÍ ---
        // Usamos PersonaFisicaDTO, que es lo que pide el método
        PersonaFisicaDTO dto = new PersonaFisicaDTO();
        dto.setCUIT("20123456789");
        dto.setPosicionIVA("Responsable Inscripto");

        // Ahora sí coinciden los tipos
        gestorContable.registrarPersonaFisica(dto, huesped);

        // Validaciones
        PersonaFisica pfGuardada = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);
        assertNotNull(pfGuardada, "Debería haberse guardado la Persona Física");
        assertEquals("20-12345678-9", pfGuardada.getCUIT());
    }

    @Test
    public void testModificarHuesped_ActualizaCuit() {
        // GIVEN
        Huesped huesped = new Huesped();
        huesped.setNombre("Modif User");
        huesped.setDocumento(TipoDoc.DNI, "87654321");
        huespedRepository.save(huesped);

        PersonaFisica pfOriginal = new PersonaFisica("Monotributo", "20-87654321-0", huesped);
        personaFisicaRepository.save(pfOriginal);

        // Preparamos DTO para cambio
        HuespedDTO dtoCambio = new HuespedDTO();
        dtoCambio.setTipo_documento(TipoDoc.DNI); // Usando Enum
        dtoCambio.setNroDocumento("87654321");
        dtoCambio.setCUIT("20-99999999-9");
        dtoCambio.setPosicionIVA("Exento");
        dtoCambio.setNombre("Modif User"); // Necesario para no romper el update

        // WHEN
        gestorContable.modificarHuesped(dtoCambio);

        // THEN
        PersonaFisica pfNueva = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);
        assertNotNull(pfNueva);
        // Validar lógica de guiones según tu implementación
        assertEquals("20-99999999-9", pfNueva.getCUIT(), "Error en actualización de CUIT");
    }

    @Test
    public void testGenerarFactura_CalculoCorrecto() throws Exception {
        // GIVEN: Huesped Adulto
        Huesped huesped = new Huesped();
        huesped.setNombre("Juan Adulto");
        huesped.setDocumento(TipoDoc.DNI, "11223344");
        // Fecha para tener > 18 años
        huesped.setFechaDeNacimiento(
                Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        huespedRepository.save(huesped);

        PersonaFisica pf = new PersonaFisica("Consumidor Final", "20-11111111-1", huesped);
        personaFisicaRepository.save(pf);

        Habitacion hab = new Habitacion();
        hab.setNumero(101);
        hab.setTipo(TipoHabitacion.IE);
        habitacionRepository.save(hab);

        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setPrecio(10000f);
        estadia.setFechaInicio(new Date());
        estadia.setFechaFin(new Date());
        estadia.setTipoHabitacion(hab.getTipo());
        estadia.agregarConsumo(new Consumo(TipoConsumo.Bar, 2000f, Moneda.ARS));
        estadiaRepository.save(estadia);

        // WHEN
        Factura factura = gestorContable.generarFacturaParaHuesped(huesped, null, estadia);

        // THEN
        assertNotNull(factura);
        assertEquals(12000f, factura.getTotalAPagar());
        assertEquals(TipoFactura.A, factura.getTipoFactura());
    }

    @Test
    public void testGenerarFactura_MenorEdad_LanzaExcepcion() {
        // GIVEN: Niño
        Huesped nino = new Huesped();
        nino.setNombre("Pepito Junior");
        nino.setDocumento(TipoDoc.DNI, "55555555");
        nino.setFechaDeNacimiento(new Date()); // Hoy = 0 años
        huespedRepository.save(nino);

        Estadia estadia = new Estadia();
        estadia.setPrecio(500f);

        // WHEN & THEN
        Exception exception = assertThrows(Exception.class, () -> {
            gestorContable.generarFacturaParaHuesped(nino, null, estadia);
        });

        assertTrue(exception.getMessage().contains("menor de edad"));
    }
}