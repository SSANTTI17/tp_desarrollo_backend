package com.desarrollo_backend.demo;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorReservas;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.modelo.habitacion.HistorialEstadoHabitacion;
import com.desarrollo_backend.demo.modelo.habitacion.EstadoHabitacion;
import com.desarrollo_backend.demo.repository.ReservaRepository;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
import com.desarrollo_backend.demo.repository.HuespedRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Calendar;

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

        @Autowired
        private HistorialEstadoHabitacionRepository historialRepo;

        @Autowired
        private HuespedRepository huespedRepo; // AGREGADO
        /*
         * @Test
         * public void buscarDisponibilidad_Exitosa()
         * String tipoString; String desdeStr; String hastaStr;
         * List<Map<String, Object>> listaResultado = new ArrayList<>();
         * 
         * TipoHabitacion tipoEnum = TipoHabitacion.fromString(tipoString);
         * if (tipoEnum == null)
         * return listaResultado;
         * 
         * Date desde = parsearFechaFront(desdeStr);
         * Date hasta = parsearFechaFront(hastaStr);
         * 
         * if (desde == null || hasta == null || desde.after(hasta))
         * return listaResultado;
         * 
         * List<Date> rango = generarRangoFechas(desde, hasta);
         * 
         * for (Date fecha : rango) {
         * boolean sd1 = verificarLibre(1, tipoEnum, fecha);
         * boolean sd2 = verificarLibre(2, tipoEnum, fecha);
         * 
         * Map<String, Object> fila = new HashMap<>();
         * fila.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(fecha));
         * fila.put("sd1", sd1);
         * fila.put("sd2", sd2);
         * 
         * listaResultado.add(fila);
         * }
         * return listaResultado;
         * }
         * 
         * /**
         * Test unitario: parseo de string a Date
         * (tipo de dato de la base de datos)
         */

        @Test
        private void parsearFechaFront_ExitoCasoGuion() {
                // String f = "2025-12-12";
                // Date resultado = gestorReservas.parsearFechaFront(f);

                // VER
        }

        /**
         * Genera un rango de fechas entre dos fechas dadas
         */
        /*
         * private List<Date> generarRangoFechas() {
         * 
         * Date d1 = new S
         * List<Date> lista = new ArrayList<>();
         * Calendar cal = Calendar.getInstance();
         * cal.setTime(d1);
         * while (!cal.getTime().after(d2)) {
         * lista.add(cal.getTime());
         * cal.add(Calendar.DAY_OF_MONTH, 1);
         * }
         * return lista;
         * }
         */

        /**
         * Verifica si una habitacion esta libre en una fecha dada
         */
        private boolean verificarLibre(int numero, TipoHabitacion tipo, Date fecha) {
                List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(numero, tipo);
                for (HistorialEstadoHabitacion h : historial) {
                        if (!fecha.before(h.getFechaInicio()) && !fecha.after(h.getFechaFin())) {
                                if (h.getEstado() == EstadoHabitacion.Ocupada
                                                || h.getEstado() == EstadoHabitacion.Reservada) {
                                        return false;
                                }
                        }
                }
                return true;
        }

        /**
         * Prueba de Integración: Verificar que la creación de una reserva
         * guarda correctamente el registro en la base de datos (H2).
         */
        @Test
        public void testCrearReserva_PersistenciaExitosa() {
                // --- ARRANGE (Preparación de datos) ---
                String apellido = "Gomez";
                String nombre = "Laura";
                String telefono = "12345678";

                int numeroHab = 101;
                String fechaInicioStr = "2026-12-15";
                String fechaFinStr = "2026-12-17";

                Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
                habitacionRepo.save(hab);
                List<Habitacion> habitaciones = new ArrayList<>();
                habitaciones.add(hab);

                // Limpiamos si existe algo previo con ese apellido
                if (!reservaRepo.findByApellido("Gomez").isEmpty()) {
                        reservaRepo.findByApellido("Gomez").forEach(reservaRepo::delete);
                }

                // --- ACT (Ejecución de la lógica completa) ---
                String resultadoOperacion = gestorReservas.crearReserva(
                                nombre, apellido, telefono, habitaciones, fechaInicioStr, fechaFinStr);

                // --- ASSERT (Verificación) ---
                assertEquals("¡Reserva Exitosa!", resultadoOperacion,
                                "El gestor debería retornar éxito al insertar.");

                // Verificar la Persistencia REAL en H2
                List<Reserva> reservas = reservaRepo.findByApellido("Gomez");
                assertFalse(reservas.isEmpty(), "La reserva no se encontró en la base de datos.");
                assertEquals(1, reservas.size(), "Se debe haber guardado una sola reserva.");
        }

        /**
         * Prueba de integración: verifico que al crear una reserva
         * también se esté creando su historial correspondiente
         */
        @Test
        public void testCrearReserva_PersistenciaExitosaHistorial() {
                // --- ARRANGE ---
                String nombre = "Laura";
                String apellido = "Gomez";
                String telefono = "12345678";

                int numeroHab = 101;
                String fechaInicioStr = "2026-12-15";
                String fechaFinStr = "2026-12-17";

                Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
                habitacionRepo.save(hab);

                if (!reservaRepo.findByApellido("Gomez").isEmpty()) {
                        reservaRepo.findByApellido("Gomez").forEach(reservaRepo::delete);
                }

                // --- ACT ---
                String resultadoOperacion = gestorReservas.crearReserva(nombre, apellido,
                                telefono, new ArrayList<>(), fechaInicioStr, fechaFinStr);

                // --- ASSERT ---
                assertEquals("¡Reserva Exitosa!", resultadoOperacion,
                                "El gestor debería retornar éxito al insertar.");

                List<Reserva> reservas = reservaRepo.findByApellido("Gomez");

                // Verificar historial
                List<HistorialEstadoHabitacion> listaEstados = historialRepo.findByHabitacion(
                                hab.getNumero(),
                                hab.getTipo());

                assertFalse(reservas.isEmpty(), "La reserva no se encontró en la base de datos.");

                assertFalse(listaEstados.isEmpty(),
                                "El Observer debería haber creado un registro en el historial.");

                HistorialEstadoHabitacion historial = listaEstados.stream()
                                .filter(h -> h.getEstado() == EstadoHabitacion.Reservada)
                                .findFirst()
                                .orElse(null);

                assertNotNull(historial,
                                "Debería existir un historial con estado 'Reservada'.");

                Reserva reservaCreada = reservas.get(0);
                // Usamos getTime() para comparar long, evitando problemas de milisegundos en
                // Date
                assertEquals(reservaCreada.getFechaIngreso().getTime(), historial.getFechaInicio().getTime(),
                                "La fecha de inicio del historial debe coincidir con la reserva.");
                assertEquals(reservaCreada.getFechaEgreso().getTime(), historial.getFechaFin().getTime(),
                                "La fecha de fin del historial debe coincidir con la reserva.");
        }

        @Test
        public void testEliminarReserva_EliminacionExitosa() {
                // Preparo la reserva a eliminar
                int numeroHab = 101;
                final Date fechaInicio;
                final Date fechaFin;
                try {
                        fechaInicio = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/15");
                        fechaFin = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/18");
                } catch (Exception e) {
                        throw new RuntimeException("Error parseando fechas en el test", e);
                }

                Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
                habitacionRepo.save(hab);
                List<Habitacion> habitacionesReservadas = new ArrayList<>();
                habitacionesReservadas.add(hab);

                // CORRECCIÓN 1: Constructor con Dirección
                Huesped huesped = new Huesped("Juan", "Perez", TipoDoc.DNI,
                                "87654321", null, "Chile", "laura@test.com", "1122334455", "Hermana", false,
                                "Calle Falsa 123");

                // CORRECCIÓN 2: Guardar Huesped ANTES de crear reserva para evitar
                // TransientPropertyValueException
                huespedRepo.save(huesped);

                Reserva reservaAEliminar = new Reserva(huesped, fechaInicio, "14:00",
                                fechaFin, "14:00",
                                habitacionesReservadas);

                reservaRepo.save(reservaAEliminar);

                // Crear manualmente el historial
                HistorialEstadoHabitacion historialCreado = new HistorialEstadoHabitacion(
                                hab, "14:00", fechaInicio, "14:00", fechaFin, EstadoHabitacion.Reservada);
                historialRepo.save(historialCreado);

                // Verificar que el historial existe antes de eliminar
                List<HistorialEstadoHabitacion> historialAntes = historialRepo.findByHabitacion(
                                hab.getNumero(), hab.getTipo());
                assertFalse(historialAntes.isEmpty());

                // Ejecuto el test
                String resultadoOperacion = gestorReservas.eliminarReserva(reservaAEliminar);

                // Verificacion
                assertEquals("Reserva eliminada con exito", resultadoOperacion);

                // Buscar la reserva eliminada
                List<Reserva> reservas = reservaRepo.findByApellido("Perez");
                assertTrue(reservas.isEmpty(), "La reserva debería haber sido eliminada de la base de datos.");

                // Verificar historial eliminado
                List<HistorialEstadoHabitacion> historialDespues = historialRepo.findByHabitacion(
                                hab.getNumero(), hab.getTipo());

                boolean existeHistorialReservada = historialDespues.stream()
                                .anyMatch(h -> h.getEstado() == EstadoHabitacion.Reservada
                                                && h.getFechaInicio().getTime() == fechaInicio.getTime()
                                                && h.getFechaFin().getTime() == fechaFin.getTime());

                assertFalse(existeHistorialReservada, "El Observer debería haber eliminado el historial.");
        }

        @Test
        public void testEliminarReserva_EliminacionExitosaHistorial() {
                // Habitacion asociada
                int numeroHab = 101;
                final Date fechaInicio;
                final Date fechaFin;
                try {
                        fechaInicio = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/15");
                        fechaFin = new SimpleDateFormat("yyyy/MM/dd").parse("2026/12/18");
                } catch (Exception e) {
                        throw new RuntimeException("Error parseando fechas en el test", e);
                }
                Habitacion hab = new Habitacion(TipoHabitacion.DE, numeroHab, 0);
                habitacionRepo.save(hab);

                List<Habitacion> habitacionesReservadas = new ArrayList<>();
                habitacionesReservadas.add(hab);

                // Huesped asociado (CORRECCIÓN: Creamos y guardamos uno real)
                Huesped huesped = new Huesped("Juan", "Perez", TipoDoc.DNI,
                                "87654321", null, "Chile", "laura@test.com", "1122334455", "Hermana", false,
                                "Calle Falsa 123");
                huespedRepo.save(huesped);

                // Creación de la reserva usando los setters (estilo DTO->Entidad)
                Reserva reservaAEliminar = new Reserva();
                reservaAEliminar.setNombre(huesped.getNombre());
                reservaAEliminar.setApellido(huesped.getApellido());
                reservaAEliminar.setTelefono(huesped.getTelefono());
                reservaAEliminar.setFechaIngreso(fechaInicio);
                reservaAEliminar.setFechaEgreso(fechaFin);
                reservaAEliminar.setHabitacionesReservadas(habitacionesReservadas);
                // Importante: No seteamos huespedRef aquí porque el test original no lo hacía,
                // PERO para que JPA no falle si hay constraint, deberíamos.
                // Si tu test original funcionaba sin setear huespedRef es porque esa columna
                // permitía NULL.
                // Si ahora falla, descomenta la línea siguiente:
                // reservaAEliminar.setHuespedRef(huesped); // Necesitarías agregar el setter en
                // Reserva.java

                reservaRepo.save(reservaAEliminar);

                // Historial
                HistorialEstadoHabitacion historialCreado = new HistorialEstadoHabitacion(
                                hab, "14:00", fechaInicio, "14:00", fechaFin, EstadoHabitacion.Reservada);
                historialRepo.save(historialCreado);

                // Ejecuto el test
                String resultadoOperacion = gestorReservas.eliminarReserva(reservaAEliminar);

                // Verificacion
                assertEquals("Reserva eliminada con exito", resultadoOperacion);

                List<Reserva> reservas = reservaRepo.findByApellido("Perez");
                assertTrue(reservas.isEmpty());

                List<HistorialEstadoHabitacion> historialDespues = historialRepo.findByHabitacion(
                                hab.getNumero(), hab.getTipo());

                boolean existeHistorialReservada = historialDespues.stream()
                                .anyMatch(h -> h.getEstado() == EstadoHabitacion.Reservada
                                                && h.getFechaInicio().getTime() == fechaInicio.getTime()
                                                && h.getFechaFin().getTime() == fechaFin.getTime());

                assertFalse(existeHistorialReservada);
        }
}