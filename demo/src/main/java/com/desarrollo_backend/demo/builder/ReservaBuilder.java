package com.desarrollo_backend.demo.builder;

import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import java.util.Date;
import java.util.List;

public class ReservaBuilder {
    
    private Reserva reserva;

    public ReservaBuilder() {
        this.reserva = new Reserva();
    }

    public ReservaBuilder conCliente(Huesped huesped) {
        reserva.setHuespedRef(huesped);
        return this;
    }

    public ReservaBuilder paraElPeriodo(Date fechaInicio, Date fechaFin) {
        reserva.setFechaIngreso(fechaInicio);
        reserva.setFechaEgreso(fechaFin);
        return this;
    }

    public ReservaBuilder conHorariosEstandar() {
        reserva.setHoraIngreso("14:00");
        reserva.setHoraEgreso("10:00");
        return this;
    }

    public ReservaBuilder asignarHabitaciones(List<Habitacion> habitaciones) {
        reserva.setHabitacionesReservadas(habitaciones);
        return this;
    }

    public Reserva build() {
        return reserva;
    }
}