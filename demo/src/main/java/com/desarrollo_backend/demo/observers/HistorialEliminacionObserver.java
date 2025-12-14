package com.desarrollo_backend.demo.observers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;

/**
 * Observador concreto que se encarga de eliminar los registros de
 * HistorialEstadoHabitacion
 * cuando una reserva es eliminada.
 */
@Component
public class HistorialEliminacionObserver implements ReservaObserver {

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepo;

    @Override
    public void onReservaEliminada(Reserva reserva) {
        // Para cada habitación asociada a la reserva
        for (Habitacion hab : reserva.getHabitacionesReservadas()) {
            // Obtener el historial de estados de la habitación
            List<HistorialEstadoHabitacion> listaEstados = historialRepo.findByHabitacion(
                    hab.getNumero(),
                    hab.getTipo());

            // Buscar y eliminar el estado "Reservada" que coincida con las fechas de la
            // reserva
            listaEstados.stream()
                    .filter(h -> h.getEstado() == EstadoHabitacion.Reservada
                            && reserva.getFechaIngreso().equals(h.getFechaInicio())
                            && reserva.getFechaEgreso().equals(h.getFechaFin()))
                    .findFirst()
                    .ifPresent(historico -> historialRepo.delete(historico));
        }
    }

    public void onReservaCreada(Reserva reserva) {

    }
}
