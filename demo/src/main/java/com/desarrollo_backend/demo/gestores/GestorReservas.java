package com.desarrollo_backend.demo.gestores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.exceptions.ReservaNotFoundException;
import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.observers.*;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
import com.desarrollo_backend.demo.repository.HuespedRepository;
import com.desarrollo_backend.demo.repository.ReservaRepository;

@Service
public class GestorReservas {

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepo;

    @Autowired
    private HabitacionRepository habitacionRepo;

    @Autowired
    private ReservaRepository reservaRepo;

    @Autowired
    private HuespedRepository huespedRepo;

    @Autowired
    private List<ReservaObserver> observers;

    /**
     * Busca la disponibilidad de habitaciones de un tipo dado para un rango de
     * fechas. Retorna una lista de pares fecha - disponibilidad.
     */
    public List<Map<String, Object>> buscarDisponibilidad(String tipoString, String desdeStr, String hastaStr) {
        List<Map<String, Object>> listaResultado = new ArrayList<>();

        TipoHabitacion tipoEnum = TipoHabitacion.fromString(tipoString);
        if (tipoEnum == null)
            return listaResultado;

        Date desde = parsearFechaFront(desdeStr);
        Date hasta = parsearFechaFront(hastaStr);

        if (desde == null || hasta == null || desde.after(hasta))
            return listaResultado;

        List<Date> rango = generarRangoFechas(desde, hasta);

        for (Date fecha : rango) {
            boolean sd1 = verificarLibre(1, tipoEnum, fecha);
            boolean sd2 = verificarLibre(2, tipoEnum, fecha);

            Map<String, Object> fila = new HashMap<>();
            fila.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(fecha));
            fila.put("sd1", sd1);
            fila.put("sd2", sd2);

            listaResultado.add(fila);
        }
        return listaResultado;
    }

    /**
     * Crea una reserva dado un huesped, tipo de habitacion, numero de habitacion,
     * fecha de inicio y fecha de fin.
     */
    @Transactional
    public String crearReserva(String nombre, String apellido, String telefono, String tipoStr,
            int numeroHab, String fechaInicioStr, String fechaFinStr) {
        try {
            Date fechaInicio = parsearFechaFront(fechaInicioStr);
            Date fechaFin = parsearFechaFront(fechaFinStr);

            if (fechaInicio == null || fechaFin == null)
                return "Error: Fechas inválidas.";

            TipoHabitacion tipoEnum = TipoHabitacion.fromString(tipoStr);
            if (tipoEnum == null)
                return "Error: Tipo de habitación inválido";

            List<Date> diasSolicitados = generarRangoFechas(fechaInicio, fechaFin);
            for (Date dia : diasSolicitados) {
                if (!verificarLibre(numeroHab, tipoEnum, dia)) {
                    String diaOcupado = new SimpleDateFormat("dd/MM/yyyy").format(dia);
                    return "Error: Habitación ocupada el día " + diaOcupado;
                }
            }

            Habitacion habitacionRef = habitacionRepo.findByIdNumeroAndIdTipo(numeroHab, tipoEnum);

            if (habitacionRef == null) {
                return "Error: La habitación no existe (Revise número y tipo)";
            }

            /*
             * // hacer reservas para más de una habitacion
             * List<Habitacion> auxList = new ArrayList<>();
             * auxList.add(habitacionRef);
             */

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setApellido(apellido);
            nuevaReserva.setNombre(nombre);
            nuevaReserva.setTelefono(telefono);
            nuevaReserva.setFechaIngreso(fechaInicio);
            nuevaReserva.setFechaEgreso(fechaFin);
            nuevaReserva.setHoraIngreso("14:00");
            nuevaReserva.setHoraEgreso("10:00");

            reservaRepo.save(nuevaReserva);

            observers.forEach(obs -> obs.onReservaCreada(nuevaReserva));

            return "¡Reserva Exitosa!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el servidor: " + e.getMessage();
        }
    }

    @Transactional
    public String crearReserva(String nombre, String apellido, String telefono, List<Habitacion> habitaciones,
            String fechaInicioStr, String fechaFinStr) {
        try {
            Date fechaInicio = parsearFechaFront(fechaInicioStr);
            Date fechaFin = parsearFechaFront(fechaFinStr);

            if (fechaInicio == null || fechaFin == null)
                return "Error: Fechas inválidas.";

            List<Date> diasSolicitados = generarRangoFechas(fechaInicio, fechaFin);

            for (Habitacion h : habitaciones) {
                for (Date dia : diasSolicitados) {
                    if (!verificarLibre(h.getNumero(), h.getTipo(), dia)) {
                        String diaOcupado = new SimpleDateFormat("dd/MM/yyyy").format(dia);
                        return "Error: Habitación " +
                                h.getNumero() + h.getNumero() + " ocupada el día " + diaOcupado;
                    }
                }

                Habitacion habitacionRef = habitacionRepo.findByIdNumeroAndIdTipo(h.getNumero(), h.getTipo());

                if (habitacionRef == null) {
                    return "Error: La habitación" + h.getNumero() + h.getTipo() + "no existe (Revise número y tipo)";
                }
            }

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setApellido(apellido);
            nuevaReserva.setNombre(nombre);
            nuevaReserva.setTelefono(telefono);
            nuevaReserva.setFechaIngreso(fechaInicio);
            nuevaReserva.setFechaEgreso(fechaFin);
            nuevaReserva.setHoraIngreso("14:00");
            nuevaReserva.setHoraEgreso("10:00");
            nuevaReserva.setHabitacionesReservadas(habitaciones);

            reservaRepo.save(nuevaReserva);

            observers.forEach(obs -> obs.onReservaCreada(nuevaReserva));

            return "¡Reserva Exitosa!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el servidor: " + e.getMessage();
        }
    }

    /**
     * Consulta reservas por huesped. Si no se especifica nombre, consulta por
     * apellido. Si no se especifica apellido, lanza ReservaNotFoundException.
     */
    public List<Reserva> consultarReservas(HuespedDTO huesped)
            throws ReservaNotFoundException {

        if (huesped.getApellido() == null || huesped.getApellido().isBlank())
            throw new ReservaNotFoundException("ingrese apellido");

        List<Reserva> reservas = new ArrayList<>();

        if (huesped.getNombre() == null || huesped.getNombre().isBlank())
            reservas = reservaRepo.findByApellido(huesped.getApellido());
        else
            reservas = reservaRepo.findByApellidoAndNombre(huesped.getApellido(), huesped.getNombre());

        if (reservas.isEmpty())
            throw new ReservaNotFoundException("no hay reservas a nombre de " + huesped.getApellido()
                    + ", " + huesped.getNombre());

        return reservas;
    }

    /**
     * Consulta reservas por habitacion, dado numero y tipo, y fecha en la bd
     */
    public Reserva consultarReservas(int numeroHabitacion, TipoHabitacion tipoHabitacion, Date fecha) {
        return reservaRepo.ReservasPorHabitacionYFecha(numeroHabitacion, tipoHabitacion, fecha);
    }

    /**
     * Elimina una lista de reservas. Actualiza observers. Transactional
     * Elimina las reservas que no se eliminaron correctamente
     */
    @Transactional
    public List<Reserva> eliminarReservas(List<Reserva> reservas) {

        List<Reserva> rebotadas = new ArrayList<>();

        for (Reserva r : reservas) {
            // CORRECCIÓN: Validar contra el mensaje real de éxito ("con exito" vs
            // "exitosamente")
            if (!eliminarReserva(r).equals("Reserva eliminada con exito")) {
                rebotadas.add(r);
            }
        }

        return rebotadas;
    }

    /**
     * Elimina una reserva específica. Actualiza observers. Transactional
     */
    @Transactional
    public String eliminarReserva(Reserva reservaEliminar) {

        if (reservaEliminar == null)
            return "Error: especifique la reserva a eliminar";

        // encuentro la reserva
        Reserva reserva = reservaRepo.findById(reservaEliminar.getId()).orElse(null);

        if (reserva != null) {
            // Notificar a todos los observadores antes de eliminar la reserva
            observers.forEach(obs -> obs.onReservaEliminada(reserva));

            reservaRepo.delete(reserva);

            return "Reserva eliminada con exito";
        }
        return "Error: Reserva no encontrada";
    }

    /**
     * Parsea una fecha desde el formato dd/MM/yyyy o yyyy-MM-dd
     * al tipo de dato usado en la base de datos
     */
    private Date parsearFechaFront(String f) {
        try {
            if (f.contains("-"))
                return new SimpleDateFormat("yyyy-MM-dd").parse(f);
            return new SimpleDateFormat("dd/MM/yyyy").parse(f);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Genera un rango de fechas entre dos fechas dadas
     */
    private List<Date> generarRangoFechas(Date d1, Date d2) {
        List<Date> lista = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        while (!cal.getTime().after(d2)) {
            lista.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lista;
    }

    /**
     * Verifica si una habitacion esta libre en una fecha dada
     */
    private boolean verificarLibre(int numero, TipoHabitacion tipo, Date fecha) {
        List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(numero, tipo);
        for (HistorialEstadoHabitacion h : historial) {
            if (!fecha.before(h.getFechaInicio()) && !fecha.after(h.getFechaFin())) {
                if (h.getEstado() == EstadoHabitacion.Ocupada || h.getEstado() == EstadoHabitacion.Reservada) {
                    return false;
                }
            }
        }
        return true;
    }
}