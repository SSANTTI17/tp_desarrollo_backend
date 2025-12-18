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
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;
import com.desarrollo_backend.demo.repository.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
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
        huesped.setNombre("Juan Adulto");
        huesped.setApellido("Perez");
        huesped.setDocumento(TipoDoc.DNI, "11223344");
        huesped.setFechaDeNacimiento(
        Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        huesped.setDireccion("hipolito 222");
        huesped.setNacionalidad("Argentino");
        huesped.setOcupacion("estudiante");
        huesped.setTelefono("123456789");
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
    public void testModificarHuesped_ActualizaCuit_Sin_Modificar_NroDoc_O_TipoDoc() {
        // GIVEN
        Huesped huesped = new Huesped();
        huesped.setNombre("Juan Adulto");
        huesped.setApellido("Perez");
        // ID Original
        huesped.setDocumento(TipoDoc.DNI, "11223344"); 
        huesped.setFechaDeNacimiento(
            Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        huesped.setDireccion("hipolito 222");
        huesped.setNacionalidad("Argentino");
        huesped.setOcupacion("estudiante");
        huesped.setTelefono("123456789");
        huesped.setAlojado(false); // Importante inicializar booleanos
        huespedRepository.save(huesped);

        // Responsable Pago Original (CUIT Viejo)
        PersonaFisica pfOriginal = new PersonaFisica("Monotributo", "20-87654321-0", huesped);
        personaFisicaRepository.save(pfOriginal);

        // Preparamos DTO para el cambio
        HuespedDTO dtoCambio = new HuespedDTO();
        
        // IMPORTANTE: Debe coincidir con el huesped creado arriba para que lo encuentre
        dtoCambio.setTipo_documento(TipoDoc.DNI); 
        dtoCambio.setNroDocumento("11223344"); 

        // Datos nuevos a impactar
        dtoCambio.setCUIT("20-99999999-9");
        dtoCambio.setPosicionIVA("Exento");
        
        // Datos rellenos (para evitar nulos si hay validaciones extra, aunque GestorContable no los usa todos)
        dtoCambio.setNombre("Juan Adulto"); 
        dtoCambio.setApellido("Perez");

        // WHEN
        // Llamamos con los nuevos argumentos: pkAnterior = null, modificoPK = false
        gestorContable.modificarHuesped(dtoCambio, null, false);

        // THEN
        // Forzamos un flush/clear del EntityManager en el test si es necesario para asegurar que leemos de BD y no de caché de nivel 1,
        // aunque findByRefHuesped suele ir a la BD.
        
        PersonaFisica pfNueva = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);
        
        assertNotNull(pfNueva, "La nueva persona física no debería ser nula");
        
        // Validamos que el CUIT haya cambiado
        assertEquals("20-99999999-9", pfNueva.getCUIT(), "Error en actualización de CUIT");
        
        // Validamos que la posición IVA también haya cambiado (opcional pero recomendado)
        assertEquals("Exento", pfNueva.getPosicionIVA(), "Error en actualización de Posición IVA");
    }

    @Test
    public void testModificarHuesped_CambioIdentidad_ReasignaResponsable() {
        // GIVEN
        
        // 1. PREPARACIÓN: Crear el Huésped "Anterior" (con DNI incorrecto o viejo)
        Huesped huespedViejo = new Huesped();
        huespedViejo.setNombre("Maria");
        huespedViejo.setApellido("Cambio");
        huespedViejo.setDocumento(TipoDoc.DNI, "11111111"); // <--- DNI VIEJO
        huespedViejo.setFechaDeNacimiento(Date.from(Instant.now()));
        huespedViejo.setDireccion("Calle 1");
        huespedViejo.setNacionalidad("Arg");
        huespedViejo.setOcupacion("Test");
        huespedViejo.setTelefono("123");
        huespedViejo.setAlojado(false);
        huespedRepository.save(huespedViejo);

        // 2. PREPARACIÓN: Asignarle un Responsable de Pago (Persona Física)
        // Usamos un CUIT que querremos conservar
        String cuit = "27-11111111-4";
        PersonaFisica pfVieja = new PersonaFisica("Monotributo", cuit, huespedViejo);
        personaFisicaRepository.save(pfVieja);

        // 3. PREPARACIÓN: Crear el Huésped "Nuevo" 
        // (Simulamos que GestorHuesped YA creó la nueva identidad en la BD antes de llamar al GestorContable)
        Huesped huespedNuevo = new Huesped();
        huespedNuevo.setNombre("Maria");
        huespedNuevo.setApellido("Cambio");
        huespedNuevo.setDocumento(TipoDoc.DNI, "22222222"); // <--- DNI NUEVO / CORREGIDO
        huespedNuevo.setFechaDeNacimiento(Date.from(Instant.now()));
        huespedNuevo.setDireccion("Calle 1");
        huespedNuevo.setNacionalidad("Arg");
        huespedNuevo.setOcupacion("Test");
        huespedNuevo.setTelefono("123");
        huespedNuevo.setAlojado(false);
        huespedRepository.save(huespedNuevo);

        // 4. PREPARACIÓN: Configurar el DTO con los datos de la NUEVA identidad
        HuespedDTO dtoCambio = new HuespedDTO();
        dtoCambio.setTipo_documento(TipoDoc.DNI);
        dtoCambio.setNroDocumento("22222222"); // Apunta al nuevo DNI
        dtoCambio.setCUIT(cuit); // Mantenemos el MISMO CUIT (esto probará que se liberó del anterior)
        dtoCambio.setPosicionIVA("Responsable Inscripto"); // Cambiamos IVA para verificar update completo

        // 5. PREPARACIÓN: Definir la PK Anterior para que el gestor sepa a quién borrarle la PF
        HuespedPK pkAnterior = new HuespedPK(TipoDoc.DNI, "11111111");

        // WHEN
        // Llamamos indicando que SÍ hubo cambio de PK (true) y pasamos la PK vieja
        gestorContable.modificarHuesped(dtoCambio, pkAnterior, true);

        // THEN
        
        // A. Validar que el huésped viejo YA NO tiene Persona Física asociada
        // (Esto confirma que el delete() funcionó y liberó el CUIT)
        PersonaFisica pfBusquedaVieja = personaFisicaRepository.findByRefHuesped(huespedViejo).orElse(null);
        assertNull(pfBusquedaVieja, "El huésped anterior (DNI 111) no debería tener responsable fiscal asociado");

        // B. Validar que el huésped nuevo AHORA TIENE la Persona Física asignada
        PersonaFisica pfBusquedaNueva = personaFisicaRepository.findByRefHuesped(huespedNuevo).orElse(null);
        assertNotNull(pfBusquedaNueva, "El nuevo huésped (DNI 222) debería tener el responsable fiscal asignado");
        
        // C. Validar integridad de los datos
        assertEquals(cuit, pfBusquedaNueva.getCUIT(), "El CUIT debe conservarse");
        assertEquals("Responsable Inscripto", pfBusquedaNueva.getPosicionIVA(), "La posición IVA debió actualizarse");
        
        // D. Validar que apunta al huésped correcto en la relación inversa
        assertEquals("22222222", pfBusquedaNueva.getHuesped().getNroDocumento());
    }

    @Test
    public void testGenerarFacturao() throws Exception {
        // GIVEN: Huesped Adulto
        Huesped huesped = new Huesped();
        huesped.setNombre("Juan Adulto");
        huesped.setApellido("Perez");
        huesped.setDocumento(TipoDoc.DNI, "11223344");
        // Fecha para tener > 18 años
        huesped.setFechaDeNacimiento(
                Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        huesped.setDireccion("hipolito 222");
        huesped.setNacionalidad("Argentino");
        huesped.setOcupacion("estudiante");
        huesped.setTelefono("123456789");
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
    public void testGenerarFactura_MenorEdad() {
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