package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.estadias.*;

public class ConsumoDTO {
    
    private TipoConsumo tipo;
    private float monto;
    private Moneda moneda;
    private Estadia estadia;

    //consutructores
    public ConsumoDTO(){}
    public ConsumoDTO(TipoConsumo tipo, float monto, Moneda moneda, Estadia estadia){
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
        this.estadia = estadia;
    }

    //getter
    public TipoConsumo getTipo() { return tipo; }
    public float getMonto() { return monto; }
    public Moneda getMoneda() { return moneda; }
    public Estadia getEstadia() { return estadia; }

    //setter
    public void setTipo(TipoConsumo tipo) { this.tipo = tipo; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setMoneda(Moneda moneda) { this.moneda = moneda; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }

}
