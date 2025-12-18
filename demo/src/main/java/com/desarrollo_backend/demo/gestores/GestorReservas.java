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

import com.desarrollo_backend.demo.builder.ReservaBuilder;
import com.desarrollo_backend.demo.exceptions.ReservaNotFoundException;
import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.observers.*;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
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
    private List<ReservaObserver> observers;

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
        List<Habitacion> habitaciones = habitacionRepo.findByIdTipo(tipoEnum);

        for (Date fecha : rango) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(fecha));

            for (Habitacion h : habitaciones) {
                boolean sd = verificarLibre(h.getNumero(), tipoEnum, fecha);
                fila.put(tipoEnum.toString() + "-" + h.getNumero(), sd);
            }
            listaResultado.add(fila);
        }
        return listaResultado;
    }

    public Reserva crearReserva(String nombre, String apellido, String telefono,
            List<Habitacion> habitacionesSolicitadas,
            String fechaInicioStr, String fechaFinStr) {
        try {
            Date fechaInicio = parsearFechaFront(fechaInicioStr);
            Date fechaFin = parsearFechaFront(fechaFinStr);

            // 1. Validaciones
            if (fechaInicio == null || fechaFin == null)
                throw new IllegalArgumentException("Fechas inválidas.");

            if (habitacionesSolicitadas == null || habitacionesSolicitadas.isEmpty())
                throw new IllegalArgumentException("No se seleccionaron habitaciones.");

            List<Date> diasSolicitados = generarRangoFechas(fechaInicio, fechaFin);
            List<Habitacion> habitacionesReales = new ArrayList<>();

            for (Habitacion habSolicitada : habitacionesSolicitadas) {
                Habitacion habBD = habitacionRepo.findByIdNumeroAndIdTipo(habSolicitada.getNumero(),
                        habSolicitada.getTipo());

                if (habBD == null)
                    throw new IllegalArgumentException("La habitación " + habSolicitada.getNumero() + " no existe.");

                for (Date dia : diasSolicitados) {
                    if (!verificarLibre(habBD.getNumero(), habBD.getTipo(), dia)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        throw new RuntimeException(
                                "Habitación " + habBD.getNumero() + " ocupada el día " + sdf.format(dia));
                    }
                }
                habitacionesReales.add(habBD);
            }

            // 2. Construcción usando el Builder
            Reserva reserva = new ReservaBuilder()
                    .conDatosCliente(nombre, apellido, telefono)
                    .paraElPeriodo(fechaInicio, fechaFin)
                    .conHorariosEstandar()
                    .asignarHabitaciones(habitacionesReales)
                    .build();

            reservaRepo.save(reserva);

            if (observers != null) {
                observers.forEach(obs -> obs.onReservaCreada(reserva));
            }

            // 3. Retorno exitoso
            return reserva;

        } catch (Exception e) {
            e.printStackTrace();
            // 4. Si falla cualquier validación o proceso, retornamos null
            return null;
        }
    }

    public List<Reserva> consultarReservas(String apellido, String nombre) throws ReservaNotFoundException {
        if (apellido == null || apellido.isBlank())
            throw new ReservaNotFoundException("Ingrese apellido");

        List<Reserva> reservas = new ArrayList<>();

        // --- CAMBIO AQUÍ: USAR BÚSQUEDA FLEXIBLE ---
        if (nombre == null || nombre.isBlank()) {
            // Busca coincidencias parciales e ignora mayúsculas en el apellido
            reservas = reservaRepo.findByApellidoContainingIgnoreCase(apellido);
        } else {
            // Busca coincidencias parciales en ambos campos
            reservas = reservaRepo.findByApellidoContainingIgnoreCaseAndNombreContainingIgnoreCase(apellido, nombre);
        }
        // --------------------------------------------

        if (reservas.isEmpty())
            throw new ReservaNotFoundException("No hay reservas a nombre de " + apellido + ", " + nombre);

        return reservas;
    }

    public Reserva consultarReservas(int numeroHabitacion, TipoHabitacion tipoHabitacion, Date fecha) {
        return reservaRepo.ReservasPorHabitacionYFecha(numeroHabitacion, tipoHabitacion, fecha);
    }

    @Transactional
    public List<Reserva> eliminarReservas(List<Reserva> reservas) {
        List<Reserva> rebotadas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (!eliminarReserva(r).equals("Reserva eliminada con exito")) {
                rebotadas.add(r);
            }
        }
        return rebotadas;
    }

    @Transactional
    public String eliminarReserva(Reserva reservaEliminar) {
        if (reservaEliminar == null)
            return "Error: especifique la reserva a eliminar";

        Reserva reserva = reservaRepo.findById(reservaEliminar.getId()).orElse(null);

        if (reserva != null) {
            observers.forEach(obs -> obs.onReservaEliminada(reserva));
            reservaRepo.delete(reserva);
            return "Reserva eliminada con exito";
        }
        return "Error: Reserva no encontrada";
    }

    private Date parsearFechaFront(String f) {
        try {
            if (f.contains("-"))
                return new SimpleDateFormat("yyyy-MM-dd").parse(f);
            return new SimpleDateFormat("dd/MM/yyyy").parse(f);
        } catch (Exception e) {
            return null;
        }
    }

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