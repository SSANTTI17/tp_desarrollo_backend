package com.desarrollo_backend.demo;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.repository.ReservaRepository;
import com.desarrollo_backend.demo.repository.HabitacionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GestorReservasTest {

    // Inyectamos el Gestor que contiene la lógica de negocio
    @Autowired
    private GestorReservas gestorReservas;

    // Inyectamos los Repositorios REALES para verificar la persistencia
    @Autowired
    private ReservaRepository reservaRepo;

    @Autowired
    private HabitacionRepository habitacionRepo;

    /**
     * Prueba de Integración: Verifica que la creación de una reserva
     * no solo pasa la lógica, sino que también guarda correctamente
     * el registro en la base de datos (H2).
     */
    @Test
    public void testCrearReserva_PersistenciaExitosa() {
        // --- ARRANGE (Preparación de datos) ---
        HuespedDTO huesped = null;

        huesped = new HuespedDTO(new Huesped("Laura", "Gomez", TipoDoc.DNI,
                "87654321", null, "Chile", "laura@test.com", "1122334455", "Hermana", false));

        int numeroHab = 101;
        String fechaInicioStr = "15/12/2026"; // Usamos una fecha futura para no chocar
        String fechaFinStr = "17/12/2026";

        Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
        habitacionRepo.save(hab);
        reservaRepo.findByApellido("Gomez").forEach(reservaRepo::delete);

        // --- ACT (Ejecución de la lógica completa) ---
        // Esto llama al GestorReservas, que utiliza el ReservaRepository REAL para
        // guardar en H2
        String resultadoOperacion = gestorReservas.crearReserva(
                huesped, TipoHabitacion.DE.toString(), numeroHab, fechaInicioStr, fechaFinStr);

        // --- ASSERT (Verificación) ---

        // 1. Verificar la respuesta del Gestor
        assertEquals("¡Reserva Exitosa!", resultadoOperacion,
                "El gestor debería retornar éxito al insertar.");

        // 2. Verificar la Persistencia REAL en H2 (La clave de la prueba de
        // integración)

        // Buscar la reserva recién creada por el apellido del huésped
        List<Reserva> reservas = reservaRepo.findByApellido("Gomez");

        // Aseguramos que se encontró exactamente UNA reserva para ese huésped
        assertFalse(reservas.isEmpty(), "La reserva no se encontró en la base de datos.");
        assertEquals(1, reservas.size(), "Se debe haber guardado una sola reserva.");

    }

    @Test
    public void testEliminarReserva_EliminacionExitosa() {

        // preparo la reserva a eliminar
        int numeroHab = 101;
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/15");
            fechaFin = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/18");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
        habitacionRepo.save(hab);
        List<Habitacion> habitacionesReservadas = new ArrayList<>();
        habitacionesReservadas.add(hab);
        Huesped huesped = new Huesped("Juan", "Perez", TipoDoc.DNI,
                "87654321", null, "Chile", "laura@test.com", "1122334455", "Hermana", false);

        Reserva reservaAEliminar = new Reserva(huesped, fechaInicio, "14:00",
                fechaFin, "14:00",
                habitacionesReservadas);

        reservaRepo.save(reservaAEliminar);

        // ejecuto el test
        String resultadoOperacion = gestorReservas.eliminarReserva(reservaAEliminar);

        // verificacion

        // 1. Verificar la respuesta del Gestor
        assertEquals("Reserva eliminada con exito", resultadoOperacion,
                "El gestor debería retornar éxito en eliminar.");

        // 2. Verificar la Persistencia REAL en H2 (La clave de la prueba de
        // integración)

        // Buscar la reserva eliminada por el apellido del huésped
        List<Reserva> reservas = reservaRepo.findByApellido("Perez");

        // Aseguramos que la reserva fue eliminada correctamente de la base de datos
        assertTrue(reservas.isEmpty(), "La reserva debería haber sido eliminada de la base de datos.");

    }

    /**
     * Prueba de Integración: Verifica que no se guarda nada si la habitación está
     * ocupada.
     */
    /**
     * @Test
     *       public void testCrearReserva_PersistenciaFallaPorOcupacion() throws
     *       Exception {
     *       // --- ARRANGE (Preparación de datos) ---
     *       // Paso 1: Crear una reserva inicial que OCUPE la habitación (Esto se
     *       guarda en la BD)
     *       HuespedDTO huesped1 = new HuespedDTO(new Huesped("Ocupante", "Inicial",
     *       TipoDoc.DNI,
     *       "11111111", null,"Pais","inicial@test.com","123","Mama",false));
     *       gestorReservas.crearReserva(huesped1, "Individual estándar", 101,
     *       "01/01/2026", "05/01/2026");
     * 
     *       // Paso 2: Intentar crear una reserva que colisiona en fechas
     *       HuespedDTO huesped2 = new HuespedDTO(new Huesped("Colision", "Fallido",
     *       TipoDoc.DNI,
     *       "22222222", null,"Pais","fallo@test.com","456","Hermano",false));
     * 
     *       String tipoStr = "Individual estándar";
     *       int numeroHab = 101;
     *       String fechaInicioStr = "02/01/2026"; // Colisiona con 01/01 a 05/01
     *       String fechaFinStr = "06/01/2026";
     * 
     *       // --- ACT (Ejecución) ---
     *       String resultadoOperacion = gestorReservas.crearReserva(
     *       huesped2, tipoStr, numeroHab, fechaInicioStr, fechaFinStr);
     * 
     *       // --- ASSERT (Verificación) ---
     * 
     *       // 1. Verificar la respuesta del Gestor
     *       assertTrue(resultadoOperacion.contains("Error: Habitación ocupada"),
     *       "El gestor debería retornar un error de ocupación.");
     * 
     *       // 2. Verificar la Persistencia REAL en H2:
     *       // Solo la reserva inicial debe estar, NO la fallida.
     *       List<Reserva> reservasColision = reservaRepo.findByApellido("Fallido");
     * 
     *       assertTrue(reservasColision.isEmpty(),
     *       "La reserva fallida NO debe haberse guardado en la base de datos.");
     *       }
     */
}