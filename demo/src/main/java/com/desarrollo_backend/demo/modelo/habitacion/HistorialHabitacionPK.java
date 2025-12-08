package com.desarrollo_backend.demo.modelo.habitacion;

import jakarta.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HistorialHabitacionPK implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoHabitacion tipo;

    @Column(name = "numero", nullable = false)
    private int numero;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    // equals & hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HistorialHabitacionPK))
            return false;
        HistorialHabitacionPK that = (HistorialHabitacionPK) o;
        return numero == that.numero &&
                tipo == that.tipo &&
                Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, numero, fecha);
    }

    // constructor vacío
    public HistorialHabitacionPK() {
    }

    // constructor
    public HistorialHabitacionPK(TipoHabitacion tipo, int numero, Date fecha) {
        this.tipo = tipo;
        this.numero = numero;
        this.fecha = fecha;
    }

    // getters
    public TipoHabitacion getTipo() {
        return tipo;
    }

    public int getNumero() {
        return numero;
    }

    public Date getFecha() {
        return fecha;
    }

    // setters
    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
