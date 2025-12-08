package com.desarrollo_backend.demo.dtos;

import java.util.Date;
import com.desarrollo_backend.demo.modelo.factura.*;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.modelo.estadias.Consumo;
import java.util.List;
import java.util.ArrayList;

public class EstadiaDTO {

    private Habitacion habitacion;
    private float precio;
    private Date fechaInicio;
    private Date fechaFin;
    private Factura factura;
    private Reserva reserva;
    private List<Consumo> consumos;

    public void agregarConsumo(Consumo c) {
        consumos.add(c);
    }

    // consutructores
    public EstadiaDTO() {
    }

    public EstadiaDTO(Reserva reserva, Date fechaInicio) {
        this.reserva = reserva;
        this.fechaInicio = fechaInicio;
        consumos = new ArrayList<>();
    }

    public EstadiaDTO(Reserva reserva, Date fechaInicio, Date fechaFin) {
        this.reserva = reserva;
        this.fechaInicio = fechaInicio;
        consumos = new ArrayList<>();
    }

    // getters
    public TipoHabitacion geTipoHabitacion() {
        return habitacion.getTipo();
    }

    public float getPrecio() {
        return precio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Factura getFactura() {
        return factura;
    }

    public Reserva getReserva() {
        return reserva;
    }

    // setters
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void geTipoHabitacion(TipoHabitacion t) {
        habitacion.setTipo(t);
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

}
