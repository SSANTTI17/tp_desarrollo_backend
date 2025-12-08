package com.desarrollo_backend.demo.dtos;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.desarrollo_backend.demo.modelo.factura.Factura;

public class NotaDeCreditoDTO {
    
    private Date fechaEmision;
    private float monto;
    private List<Factura> facturasAsociada;

    //constructores
    public NotaDeCreditoDTO(){}   
    public NotaDeCreditoDTO(Date fechaEmision, float monto, Factura facturasAsociada){
        this.fechaEmision = fechaEmision;
        this.monto = monto;
        this.facturasAsociada = new ArrayList<>();
        this.facturasAsociada.add(facturasAsociada);
    }

    //getters y setters
    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }
    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }
    public List<Factura> getFacturasAsociadas() { return facturasAsociada; }
    public void addFacturaAsociada(Factura facturaAsociada) { this.facturasAsociada.add(facturaAsociada); }
}