package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.estadias.*;

public class ConsumoDTO {
    private int id;
    private TipoConsumo tipo;
    private float monto;
    private Moneda moneda;
    private EstadiaDTO estadiaDTO;

    //consutructores
    public ConsumoDTO(){}
    public ConsumoDTO(TipoConsumo tipo, float monto, Moneda moneda, EstadiaDTO estadia){
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
        this.estadiaDTO = estadia;
    }

    public ConsumoDTO(Consumo consumo){
        this.id = consumo.getId();
        this.tipo = consumo.getTipo();
        this.monto = consumo.getMonto();
        this.moneda = consumo.getMoneda();
        this.estadiaDTO = this.transformarDTO(consumo.getEstadia());
    }

    //getter
    public int getId() { return id; }
    public TipoConsumo getTipo() { return tipo; }
    public float getMonto() { return monto; }
    public Moneda getMoneda() { return moneda; }
    public EstadiaDTO getEstadia() { return estadiaDTO; }

    //setter
    private EstadiaDTO transformarDTO(Estadia estadia) {
        return new EstadiaDTO(estadia);
    }
    public void setId(int id) { this.id = id; }
    public void setTipo(TipoConsumo tipo) { this.tipo = tipo; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setMoneda(Moneda moneda) { this.moneda = moneda; }
    public void setEstadia(EstadiaDTO estadia) { this.estadiaDTO = estadia; }

}
