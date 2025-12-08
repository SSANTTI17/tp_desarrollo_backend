package com.desarrollo_backend.demo.modelo.habitacion;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class HabitacionPK implements Serializable {
    @Enumerated(EnumType.STRING)
    private TipoHabitacion tipo;

    @Column(nullable = false)
    private int numero;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HabitacionPK))
            return false;
        HabitacionPK that = (HabitacionPK) o;
        return Objects.equals(numero, that.numero) && Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, numero);
    }

    // getter
    public TipoHabitacion getTipo() {
        return tipo;
    }

    public int getNumero() {
        return numero;
    }

    // setter
    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
