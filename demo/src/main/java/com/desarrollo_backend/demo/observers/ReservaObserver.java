package com.desarrollo_backend.demo.observers;

import com.desarrollo_backend.demo.modelo.habitacion.*;

/**
 * Interfaz Observer para el patrón Observer aplicado a la eliminación de
 * reservas.
 * Los observadores que implementen esta interfaz serán notificados cuando una
 * reserva sea eliminada.
 */
public interface ReservaObserver {

    /**
     * Método que se invoca cuando una reserva es eliminada.
     * 
     * @param reserva La reserva que ha sido eliminada
     */
    void onReservaEliminada(Reserva reserva);

    void onReservaCreada(Reserva reserva);
}
