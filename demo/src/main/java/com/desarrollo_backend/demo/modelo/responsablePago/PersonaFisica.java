package com.desarrollo_backend.demo.modelo.responsablePago;

import com.desarrollo_backend.demo.modelo.huesped.Huesped;

import jakarta.persistence.*;

@Entity
@Table(name = "personas_fisicas")
public class PersonaFisica extends ResponsablePago {

    @Column(nullable = false, length = 100)
    private String PosicionIVA;

    @OneToOne
    private Huesped RefHuesped;

    protected PersonaFisica() {
    }

    public PersonaFisica(String PosicionIVA, String CUIT, Huesped RefHuesped) {
        super(CUIT);
        this.PosicionIVA = PosicionIVA;
        this.RefHuesped = RefHuesped;
    }

    public String getPosicionIVA() {
        return PosicionIVA;
    }

    public Huesped getHuesped() {
        return RefHuesped;
    }

    public void setPosicionIVA(String posicionIVA) {
        this.PosicionIVA = posicionIVA;
    }
}