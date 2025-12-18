package com.desarrollo_backend.demo;

import com.desarrollo_backend.demo.modelo.estadias.Consumo;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.modelo.factura.TipoFactura;
import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.dtos.LocalidadDTO;
import com.desarrollo_backend.demo.dtos.ProvinciaDTO;
import com.desarrollo_backend.demo.dtos.DireccionDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FacturaDTOTest {

    // 1. Testear Constructor: Entidad -> DTO
    // Este verifica que cuando sacas datos de la BD para mandarlos al front, no se
    // pierda nada.
    @Test
    void testConstructorDesdeEntidad_MapeaCorrectamente() {
        // GIVEN: Una entidad Factura cargada
        Factura entidad = new Factura();
        entidad.setId(1);
        entidad.setTipoFactura(TipoFactura.A); // Asumiendo que tenés este Enum
        entidad.setValorEstadia(10000f);
        entidad.setTotalAPagar(12100f);
        entidad.setVuelto(0f);
        entidad.setPagado(true);

        // Mockeamos el ResponsablePago para no depender de su lógica interna
        ResponsablePago responsableMock = Mockito.mock(ResponsablePago.class);
        entidad.setResponsablePago(responsableMock);

        // WHEN: Creamos el DTO
        FacturaDTO dto = new FacturaDTO(entidad);

        // THEN: Validamos los campos
        assertEquals(TipoFactura.A, dto.getTipoFactura());
        assertEquals(10000f, dto.getValorEstadia());
        assertEquals(12100f, dto.getTotalAPagar());
        assertEquals(0f, dto.getVuelto());
        assertTrue(dto.getPagado());

        // Validamos que el objeto responsable sea el mismo (referencia)
        assertEquals(responsableMock, dto.getResponsablePago());
    }

    // 2. Testear Constructor: DTO -> Entidad
    // Este verifica que cuando recibís datos del front, se conviertan bien para
    // guardar en BD.
    @Test
    void testConstructorEntidadDesdeDTO_MapeaCorrectamente() {
        // GIVEN: Un DTO simulando datos que vienen del Front
        FacturaDTO dto = new FacturaDTO();
        dto.setTipoFactura(TipoFactura.B);
        dto.setValorEstadia(5000f);
        dto.setTotalAPagar(5000f);
        dto.setVuelto(500f);
        dto.setPagado(false);

        ResponsablePago responsableMock = Mockito.mock(ResponsablePago.class);
        dto.setResponsablePago(responsableMock);

        // WHEN: Usamos el constructor de la Entidad que recibe un DTO
        Factura entidad = new Factura(dto, null);

        // THEN
        assertEquals(TipoFactura.B, entidad.getTipoFactura());
        assertEquals(5000f, entidad.getValorEstadia());
        assertEquals(5000f, entidad.getTotalAPagar());
        assertEquals(500f, entidad.getVuelto());
        assertFalse(entidad.getPagado());
        assertEquals(responsableMock, entidad.getResponsablePago());
    }

    // 3. Testear Getters y Setters (Básico)
    @Test
    void testSettersYGetters() {
        FacturaDTO dto = new FacturaDTO();

        dto.setTotalAPagar(999.99f);
        dto.setPagado(true);
        dto.setTipoFactura(TipoFactura.B);

        assertEquals(999.99f, dto.getTotalAPagar(), 0.001);
        assertTrue(dto.getPagado());
        assertEquals(TipoFactura.B, dto.getTipoFactura());
    }
}

class EstadiaDTOTest {

    // 1. Testear Constructor que convierte Entidad -> DTO (El más importante)
    @Test
    void testConstructorDesdeEntidad_MapeaTodosLosCampos() {
        // GIVEN: Una entidad Estadia con datos completos
        Estadia entidad = new Estadia();
        entidad.setId(100);
        entidad.setPrecio(15000f);

        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        entidad.setFechaInicio(fechaInicio);
        entidad.setFechaFin(fechaFin);

        Habitacion hab = new Habitacion(TipoHabitacion.DE, 202, 5000f);
        entidad.setHabitacion(hab);

        Reserva reserva = new Reserva();
        entidad.setReserva(reserva);

        // Simulamos consumos en la entidad
        Consumo c1 = new Consumo();
        entidad.agregarConsumo(c1);

        // WHEN: Creamos el DTO
        EstadiaDTO dto = new EstadiaDTO(entidad);

        // THEN: Verificamos que todo se copió bien
        assertEquals(100, dto.getId());
        assertEquals(15000f, dto.getPrecio());
        assertEquals(fechaInicio, dto.getFechaInicio());
        assertEquals(fechaFin, dto.getFechaFin());
        assertEquals(TipoHabitacion.DE, dto.geTipoHabitacion()); // Nota: Usé tu nombre con typo 'geTipo'

        assertEquals(reserva, dto.getReserva());

        // Verificamos la lista (asumiendo que agregás el getter al DTO, ver nota abajo)
        // assertEquals(1, dto.getConsumos().size());
    }

    // 2. Testear Constructor para Nueva Estadia (Reserva + Fechas)
    @Test
    void testConstructorDesdeReserva_InicializaLista() {
        Reserva r = new Reserva();
        Date inicio = new Date();

        EstadiaDTO dto = new EstadiaDTO(r, inicio);

        assertEquals(r, dto.getReserva());
        assertEquals(inicio, dto.getFechaInicio());

        // Probamos agregar consumo para asegurar que la lista no es null
        // Si la lista fuera null, esto lanzaría NullPointerException
        Consumo c = new Consumo();
        assertDoesNotThrow(() -> dto.agregarConsumo(c));
    }

    // 3. Testear Setters y Getters simples
    @Test
    void testSettersYGetters() {
        EstadiaDTO dto = new EstadiaDTO();

        // Como el constructor vacío no inicia la lista en tu código,
        // cuidado al usar agregarConsumo aquí si no arreglas el DTO.

        dto.setId(50);
        dto.setPrecio(99.9f);
        Date hoy = new Date();
        dto.setFechaFin(hoy);

        assertEquals(50, dto.getId());
        assertEquals(99.9f, dto.getPrecio(), 0.001);
        assertEquals(hoy, dto.getFechaFin());
    }
}

class LocalidadDTOTest {

    // 1. Testear Constructor con Parámetros (Lógica de inicialización)
    @Test
    void testConstructorParametrizado_InicializaCorrectamente() {
        // GIVEN
        String nombre = "Paraná";
        // Asumimos que ProvinciaDTO existe en tu proyecto, si no, pasamos null para
        // probar
        ProvinciaDTO provinciaDummy = new ProvinciaDTO();

        // WHEN
        LocalidadDTO dto = new LocalidadDTO(nombre, provinciaDummy);

        // THEN
        assertEquals("Paraná", dto.getNombre());
        assertEquals(provinciaDummy, dto.getProvincia());

        // VERIFICACIÓN CLAVE: La lista no debe ser null, debe estar vacía
        assertNotNull(dto.getDirecciones(), "La lista de direcciones debe inicializarse en el constructor");
        assertTrue(dto.getDirecciones().isEmpty(), "La lista de direcciones debe estar vacía al inicio");
    }

    // 2. Testear Constructor Vacío
    @Test
    void testConstructorVacio() {
        LocalidadDTO dto = new LocalidadDTO();
        assertNull(dto.getNombre());
        assertNull(dto.getDirecciones()); // En el constructor vacío NO inicializas la lista
    }

    // 3. Testear Setters y Getters y manejo de Listas
    @Test
    void testSettersYGetters_Direcciones() {
        // GIVEN
        LocalidadDTO dto = new LocalidadDTO();
        dto.setNombre("Santa Fe");

        List<DireccionDTO> listaDirecciones = new ArrayList<>();
        DireccionDTO dir1 = new DireccionDTO();
        listaDirecciones.add(dir1);

        // WHEN
        dto.setDirecciones(listaDirecciones);

        // THEN
        assertEquals("Santa Fe", dto.getNombre());
        assertNotNull(dto.getDirecciones());
        assertEquals(1, dto.getDirecciones().size());
    }
}
