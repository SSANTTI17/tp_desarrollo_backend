package com.desarrollo_backend.demo.modelo.estadias;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "consumos")
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConsumo tipo;

    @Column(nullable = false)
    private float monto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Column(nullable = false)
    private Boolean facturado = false;

    @ManyToOne
    @JoinColumn(name = "estadia_id")
    @JsonIgnore
    private Estadia estadia;

    // consutructores
    public Consumo() {
    }

    public Consumo(TipoConsumo tipo, float monto, Moneda moneda) {
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
    }

    // getter
    public int getId() {
        return id;
    }

    public TipoConsumo getTipo() {
        return tipo;
    }

    public float getMonto() {
        return monto;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public boolean isFacturado() {
        return facturado;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(TipoConsumo tipo) {
        this.tipo = tipo;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public void setFacturado(boolean facturado) {
        this.facturado = facturado;
    }

}
