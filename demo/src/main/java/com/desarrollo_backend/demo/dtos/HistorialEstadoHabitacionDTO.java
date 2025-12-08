package com.desarrollo_backend.demo.dtos;

import java.util.Date;
import com.desarrollo_backend.demo.modelo.habitacion.*;

public class HistorialEstadoHabitacionDTO {
    
    private TipoHabitacion tipo;
    private int numero;
    private Date fechaInicio;
    private Habitacion habitacion;
    private String horaInicio;
    private Date fechaFin;
    private String horaFin;
    private EstadoHabitacion estado;

    //all args constructor
    public HistorialEstadoHabitacionDTO(TipoHabitacion tipo, int numero, Date fechaInicio, Habitacion habitacion, String horaInicio, Date fechaFin, String horaFin, EstadoHabitacion estado) {
        this.tipo = tipo;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.habitacion = habitacion;
        this.horaInicio = horaInicio;
        this.fechaFin = fechaFin;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    //setter
    public void setTipo(TipoHabitacion tipo) { this.tipo = tipo; }
    public void setNumero(int numero) { this.numero = numero; }
    public void setFecha(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }

    //getter
    public TipoHabitacion getTipo() { return tipo; }
    public int getNumero() { return numero; }
    public Date getFecha() { return fechaInicio; }
    public Habitacion getHabitacion() { return habitacion; }
    public String getHoraInicio() { return horaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public String getHoraFin() { return horaFin; }
    public EstadoHabitacion getEstado() { return estado; }
}