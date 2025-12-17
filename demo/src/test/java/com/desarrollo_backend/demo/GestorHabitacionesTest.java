package com.desarrollo_backend.demo;

import com.desarrollo_backend.demo.gestores.GestorHabitaciones;
import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
import com.desarrollo_backend.demo.repository.HuespedRepository;
import com.desarrollo_backend.demo.dtos.OcuparDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GestorHabitacionesTest {

    @Autowired
    private GestorHabitaciones gestorHabitaciones;

    @Autowired
    private HabitacionRepository habitacionRepo;

    @Autowired
    private HuespedRepository huespedRepo;

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepo;

    // --- TEST 1: REGISTRAR OCUPACIÓN ---
    @Test
    public void testRegistrarOcupacion_Exito() throws Exception {
        // 1. Arrange
        Habitacion hab = new Habitacion(TipoHabitacion.DE, 201, 5000f);
        habitacionRepo.save(hab);

        Huesped huesped = new Huesped("Carlos", "Test", TipoDoc.DNI, "12345", new Date(), "Arg", "mail", "111", "Ocup",
                false, "Dir", false);
        huespedRepo.save(huesped);

        OcuparDTO ocuparDTO = new OcuparDTO();
        ocuparDTO.setNumeroHabitacion(201);
        ocuparDTO.setTipoHabitacion("DE");
        ocuparDTO.setFechaInicio("2026-01-01");
        ocuparDTO.setFechaFin("2026-01-05");

        List<HuespedDTO> listaHuespedes = new ArrayList<>();
        HuespedDTO hDto = new HuespedDTO();
        hDto.setTipo_documento(TipoDoc.DNI);
        hDto.setNroDocumento("12345");
        listaHuespedes.add(hDto);

        ocuparDTO.setHuespedes(listaHuespedes);

        // 2. Act
        gestorHabitaciones.registrarOcupacion(ocuparDTO);

        // 3. Assert
        List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(201, TipoHabitacion.DE);
        assertFalse(historial.isEmpty());
        assertEquals(EstadoHabitacion.Ocupada, historial.get(0).getEstado());

        Huesped huespedActualizado = huespedRepo.findById(huesped.getId()).orElse(null);
        assertNotNull(huespedActualizado);
        assertTrue(huespedActualizado.isAlojado());
    }

    // --- TEST 2: MOSTRAR ESTADO HABITACIONES ---
    @Test
    public void testMostrarEstadoHabitaciones_RetornaListaCorrecta() {
        Habitacion hab = new Habitacion(TipoHabitacion.IE, 105, 3000f);
        habitacionRepo.save(hab);

        Date fechaOcupada = Date.from(LocalDate.of(2026, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HistorialEstadoHabitacion hist = new HistorialEstadoHabitacion(hab, "10:00", fechaOcupada, "10:00",
                fechaOcupada, EstadoHabitacion.Ocupada);
        historialRepo.save(hist);

        LocalDate inicio = LocalDate.of(2026, 2, 1);
        LocalDate fin = LocalDate.of(2026, 2, 2);

        List<HabitacionDTO> resultado = gestorHabitaciones.mostrarEstadoHabitaciones(inicio, fin);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        HabitacionDTO dtoEncontrado = resultado.stream()
                .filter(d -> d.getNumero() == 105)
                .findFirst()
                .orElse(null);

        assertNotNull(dtoEncontrado);
        assertEquals(2, dtoEncontrado.getEstadosPorDia().size());
    }

    // --- TEST 3: RESERVAR HABITACIÓN ---
    @Test
    public void testReservarHabitacion_GuardaHistorial() {
        Habitacion hab = new Habitacion(TipoHabitacion.DE, 301, 6000f);
        habitacionRepo.save(hab);

        Date inicio = new Date();
        Date fin = new Date();

        gestorHabitaciones.reservarHabitacion(301, TipoHabitacion.DE, inicio, "14:00", fin, "10:00");

        List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(301, TipoHabitacion.DE);
        assertFalse(historial.isEmpty());
        assertEquals(EstadoHabitacion.Reservada, historial.get(0).getEstado());
    }

    // --- TEST 4: OCUPAR HABITACIÓN ---
    @Test
    public void testOcuparHabitacion_GuardaHistorial() {
        Habitacion hab = new Habitacion(TipoHabitacion.DE, 302, 6000f);
        habitacionRepo.save(hab);

        Date inicio = new Date();
        Date fin = new Date();

        gestorHabitaciones.ocuparHabitacion(302, TipoHabitacion.DE, inicio, "14:00", fin, "10:00");

        List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(302, TipoHabitacion.DE);
        assertFalse(historial.isEmpty());
        assertEquals(EstadoHabitacion.Ocupada, historial.get(0).getEstado());
    }

    // --- TEST 5: VERIFICAR DISPONIBILIDAD (AQUÍ ESTÁ EL CAMBIO) ---
    @Test
    public void testVerificarDisponibilidad_DetectaOcupacion() {
        // 1. Arrange
        Habitacion hab = new Habitacion(TipoHabitacion.DE, 401, 5000f);
        habitacionRepo.save(hab);

        Date fechaCheck = Date.from(LocalDate.of(2026, 5, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        HistorialEstadoHabitacion ocupacion = new HistorialEstadoHabitacion(
                hab, "10:00", fechaCheck, "10:00", fechaCheck, EstadoHabitacion.Ocupada);
        historialRepo.save(ocupacion);

        // CORRECCIÓN AQUÍ: Constructor (Tipo, Numero, Fecha)
        // Pasamos new Date() como fecha dummy porque el gestor usa un rango, no la
        // fecha del PK
        HistorialHabitacionPK pk = new HistorialHabitacionPK(TipoHabitacion.DE, 401, new Date());

        // 2. Act
        EstadoHabitacion estado = gestorHabitaciones.verificarDisponibilidad(pk, fechaCheck, fechaCheck);

        // 3. Assert
        assertEquals(EstadoHabitacion.Ocupada, estado);
    }

    @Test
    public void testVerificarDisponibilidad_DisponibleSiNoHayNada() {
        // 1. Arrange
        Habitacion hab = new Habitacion(TipoHabitacion.DE, 402, 5000f);
        habitacionRepo.save(hab);

        // CORRECCIÓN AQUÍ TAMBIÉN
        HistorialHabitacionPK pk = new HistorialHabitacionPK(TipoHabitacion.DE, 402, new Date());
        Date fecha = new Date();

        // 2. Act
        EstadoHabitacion estado = gestorHabitaciones.verificarDisponibilidad(pk, fecha, fecha);

        // 3. Assert
        assertEquals(EstadoHabitacion.Disponible, estado);
    }

    // --- TEST 6: ERROR ---
    @Test
    public void testRegistrarOcupacion_HabitacionNoExiste_LanzaExcepcion() {
        OcuparDTO dto = new OcuparDTO();
        dto.setNumeroHabitacion(9999);
        dto.setTipoHabitacion("DE");
        dto.setFechaInicio("2026-01-01");
        dto.setFechaFin("2026-01-02");
        dto.setHuespedes(new ArrayList<>());

        assertThrows(RuntimeException.class, () -> {
            gestorHabitaciones.registrarOcupacion(dto);
        });
    }
}