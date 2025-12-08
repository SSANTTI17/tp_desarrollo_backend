package com.desarrollo_backend.demo.modelo.estadias;

import jakarta.persistence.*;

@Entity
@Table (name = "consumos")
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

    @ManyToOne
    @JoinColumn(name = "estadia_id")
    private Estadia estadia;
    
    //consutructores
    public Consumo(){}
    public Consumo(TipoConsumo tipo, float monto, Moneda moneda){
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
    }

    //getter
    public TipoConsumo getTipo() { return tipo; }
    public float getMonto() { return monto; }
    public Moneda getMoneda() { return moneda; }

    //setter
    public void setTipo(TipoConsumo tipo) { this.tipo = tipo; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setMoneda(Moneda moneda) { this.moneda = moneda; }

}
