package com.desarrollo_backend.demo.observers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.desarrollo_backend.demo.modelo.habitacion.EstadoHabitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.HistorialEstadoHabitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
import com.desarrollo_backend.demo.repository.HabitacionRepository;

public class HistorialCreacionObserver implements ReservaObserver {

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepo;

    @Autowired
    private HabitacionRepository habitacionRepo;

    /**
     * Al crear una reserva, para cada habitacion asociada
     * se le crea su HistorialEstadoHabitacion en estado reservado
     * correspondiente
     */
    @Override
    public void onReservaCreada(Reserva reserva) {

        Date auxIngreso = reserva.getFechaIngreso();
        Date auxEgreso = reserva.getFechaEgreso();

        for (Habitacion hab : reserva.getHabitacionesReservadas()) {

            Habitacion habitacionRef = habitacionRepo.findByIdNumeroAndIdTipo(hab.getNumero(), hab.getTipo());

            HistorialEstadoHabitacion nuevo = new HistorialEstadoHabitacion(
                    habitacionRef,
                    "14:00",
                    auxIngreso,
                    "10:00",
                    auxEgreso,
                    EstadoHabitacion.Reservada);

            historialRepo.save(nuevo);

        }
    }

    @Override
    public void onReservaEliminada(Reserva reserva) {

    }

}
