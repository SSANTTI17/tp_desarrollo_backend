package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.estadias.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConsumoDTO {
    private int id;
    private TipoConsumo tipo;
    private float monto;
    private Moneda moneda;

    @JsonIgnore // rompe bucle infinito de JSON (Serialización)
    private EstadiaDTO estadiaDTO;

    // constructores
    public ConsumoDTO() {
    }

    public ConsumoDTO(TipoConsumo tipo, float monto, Moneda moneda, EstadiaDTO estadia) {
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
        this.estadiaDTO = estadia;
    }

    public ConsumoDTO(Consumo consumo) {
        this.id = consumo.getId();
        this.tipo = consumo.getTipo();
        this.monto = consumo.getMonto();
        this.moneda = consumo.getMoneda();

        // --- CORRECCIÓN CRÍTICA ---
        // Eliminamos la conversión recursiva de la estadía.
        // Al tener @JsonIgnore, no necesitamos cargar este objeto pesado aquí
        // y evitamos el StackOverflowError.
        this.estadiaDTO = null;
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

    public EstadiaDTO getEstadia() {
        return estadiaDTO;
    }

    // setter
    // Eliminamos transformarDTO() porque ya no se usa y es peligroso

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

    public void setEstadia(EstadiaDTO estadia) {
        this.estadiaDTO = estadia;
    }

}