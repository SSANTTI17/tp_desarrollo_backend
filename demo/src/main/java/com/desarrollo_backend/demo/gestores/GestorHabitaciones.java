package com.desarrollo_backend.demo.gestores;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.repository.HuespedRepository;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;

import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.modelo.huesped.*;

import com.desarrollo_backend.demo.dtos.OcuparDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;

import java.text.SimpleDateFormat;

@Service
public class GestorHabitaciones {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Transactional
    public void registrarOcupacion(OcuparDTO ocupacion) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date inicio = sdf.parse(ocupacion.getFechaInicio());
        Date fin = sdf.parse(ocupacion.getFechaFin());
        TipoHabitacion tipo = TipoHabitacion.valueOf(ocupacion.getTipoHabitacion());

        Habitacion hab = habitacionRepository.findByIdNumeroAndIdTipo(ocupacion.getNumeroHabitacion(), tipo);
        if (hab == null)
            throw new RuntimeException("Habitación no encontrada");

        HistorialEstadoHabitacion nuevoEstado = new HistorialEstadoHabitacion(
                hab,
                "00:00",
                inicio,
                "23:59",
                fin,
                EstadoHabitacion.Ocupada);
        historialRepository.save(nuevoEstado);

        for (HuespedDTO hDto : ocupacion.getHuespedes()) {
            HuespedPK pk = new HuespedPK(hDto.getTipo_documento(), hDto.getNroDocumento());
            Huesped huesped = huespedRepository.findById(pk).orElse(null);

            if (huesped != null) {
                huesped.setAlojado(true);
                huespedRepository.save(huesped);
            }
        }
    }

    // 1) MOSTRAR ESTADO DE HABITACIONES ENTRE FECHAS

    @Transactional(readOnly = true)
    public List<HabitacionDTO> mostrarEstadoHabitaciones(LocalDate fechaInicio, LocalDate fechaFin) {

        List<Habitacion> habitaciones = habitacionRepository.findAll();
        List<HabitacionDTO> dtos = new ArrayList<>();

        // Generamos la lista de fechas entre inicio y fin
        List<Date> rangoFechas = generarRangoFechas(fechaInicio, fechaFin);

        for (Habitacion h : habitaciones) {

            HabitacionDTO dto = new HabitacionDTO();
            dto.setNumero(h.getNumero());
            dto.setTipo(h.getTipo());
            dto.setCostoNoche(h.getCostoNoche());

            List<EstadoHabitacion> estados = new ArrayList<>();
            
            // Para cada día, preguntamos el estado a la entidad
            for (Date fecha : rangoFechas) {
                estados.add(h.getEstadoEnFecha(fecha));
            }

            dto.setEstadosPorDia(estados);
            dtos.add(dto);
        }
        return dtos;
    }

    // 2) OCUPAR HABITACIÓN
    @Transactional
    public void ocuparHabitacion(int numero, TipoHabitacion tipo,
            Date fechaInicio, String horaInicio,
            Date fechaFin, String horaFin) {

        Habitacion habitacion = habitacionRepository.findByIdNumeroAndIdTipo(numero, tipo);

        if (habitacion == null) {
            throw new RuntimeException("Habitación no encontrada");
        }

        HistorialEstadoHabitacion historial = new HistorialEstadoHabitacion(
                habitacion,
                horaInicio,
                fechaInicio,
                horaFin,
                fechaFin,
                EstadoHabitacion.Ocupada);

        historialRepository.save(historial);
    }

    // 3) RESERVAR HABITACIÓN
    @Transactional
    public void reservarHabitacion(int numero, TipoHabitacion tipo,
            Date fechaInicio, String horaInicio,
            Date fechaFin, String horaFin) {

        Habitacion habitacion = habitacionRepository.findByIdNumeroAndIdTipo(numero, tipo);

        if (habitacion == null) {
            throw new RuntimeException("Habitación no encontrada");
        }

        HistorialEstadoHabitacion historial = new HistorialEstadoHabitacion(
                habitacion,
                horaInicio,
                fechaInicio,
                horaFin,
                fechaFin,
                EstadoHabitacion.Reservada);

        historialRepository.save(historial);
    }

    // 4) VERIFICAR DISPONIBILIDAD ENTRE FECHAS
    @Transactional(readOnly = true)
    public EstadoHabitacion verificarDisponibilidad(
            HistorialHabitacionPK id,
            Date fechaInicio,
            Date fechaFin) {

        List<HistorialEstadoHabitacion> historial = historialRepository.buscarEstadosEnRango(id.getNumero(),
                id.getTipo(), fechaInicio, fechaFin);

        if (historial == null || historial.isEmpty()) {
            return EstadoHabitacion.Disponible;
        }

        for (HistorialEstadoHabitacion h : historial) {
            if (h.getEstado() == EstadoHabitacion.Ocupada) {
                return EstadoHabitacion.Ocupada;
            }
            if (h.getEstado() == EstadoHabitacion.Reservada) {
                return EstadoHabitacion.Reservada;
            }
        }
        return EstadoHabitacion.Disponible;
    }

    // 5) GENERAR LISTA DE FECHAS ENTRE INICIO–FIN
    private List<Date> generarRangoFechas(LocalDate start, LocalDate end) {
        List<Date> dates = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            // Convertimos LocalDate a Date usando el inicio del día en la zona horaria del
            // sistema
            dates.add(Date.from(current.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            current = current.plusDays(1);
        }
        return dates;
    }

}
