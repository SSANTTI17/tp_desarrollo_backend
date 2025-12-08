package com.desarrollo_backend.demo.modelo.habitacion;

import java.util.Date;
import jakarta.persistence.*;
import com.desarrollo_backend.demo.dtos.HistorialEstadoHabitacionDTO;

@Entity
@Table(name = "estados")
public class HistorialEstadoHabitacion {

    @EmbeddedId
    private HistorialHabitacionPK id;

    @ManyToOne
    @MapsId("habitacionPK")
    @JoinColumns({
            @JoinColumn(name = "numero", referencedColumnName = "numero"),
            @JoinColumn(name = "tipo", referencedColumnName = "tipo")
    })
    private Habitacion habitacion;

    @Column(nullable = false)
    private String horaInicio;

    @Column(nullable = true)
    private Date fechaFin;

    @Column(nullable = true)
    private String horaFin;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado;

    public HistorialEstadoHabitacion() {
        this.id = new HistorialHabitacionPK();
    }

    public HistorialEstadoHabitacion(HistorialEstadoHabitacionDTO h) {
        this.id = new HistorialHabitacionPK();
        id.setTipo(h.getTipo());
        id.setNumero(h.getNumero());
        id.setFecha(h.getFecha());
        this.habitacion = h.getHabitacion();
        this.horaInicio = h.getHoraInicio();
        this.fechaFin = h.getFechaFin();
        this.horaFin = h.getHoraFin();
        this.estado = h.getEstado();
    }

    public HistorialEstadoHabitacion(Habitacion h, String horaInicio, Date fechaInicio, EstadoHabitacion estado) {
        this.id = new HistorialHabitacionPK();
        this.habitacion = h;
        id.setNumero(h.getNumero());
        id.setTipo(h.getTipo());
        this.horaInicio = horaInicio;
        id.setFecha(fechaInicio);
        this.estado = estado;
    }

    public HistorialEstadoHabitacion(Habitacion h, String horaInicio, Date fechaInicio, String horaFin, Date fechaFin,
            EstadoHabitacion estado) {
        this.id = new HistorialHabitacionPK();
        this.habitacion = h;
        id.setNumero(h.getNumero());
        id.setTipo(h.getTipo());
        this.horaInicio = horaInicio;
        id.setFecha(fechaInicio);
        this.fechaFin = fechaFin;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    // getter
    public HistorialHabitacionPK getId() {
        return id;
    }

    public TipoHabitacion getTipo() {
        return id.getTipo();
    }

    public int getNumero() {
        return id.getNumero();
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public Date getFechaInicio() {
        return id.getFecha();
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    // setter
    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public void setTipo(TipoHabitacion tipo) {
        id.setTipo(tipo);
    }

    public void setNumero(int numero) {
        id.setNumero(numero);
    }

    public void setFechaInicio(Date fechaInicio) {
        id.setFecha(fechaInicio);
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

}
