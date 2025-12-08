package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.factura.*;

public class FormaDePagoDTO {
    
    private Banco tarjetaDeCredito;    
    private Banco tarjetaDeDebito;    
    private boolean efectivo;
    private float monto;
    private Factura factura;

    public FormaDePagoDTO(){}

    public FormaDePagoDTO(Banco credito, Banco debito, float monto){
        
        tarjetaDeCredito = credito;
        tarjetaDeDebito = debito;
        this.efectivo = false;
        this.monto = monto;

    }

    public FormaDePagoDTO(float monto){
        
        tarjetaDeCredito =null;
        tarjetaDeDebito = null;
        this.efectivo = true;
        this.monto = monto;

    }    

    //getters y setters
    public Banco getTarjetaDeCredito() { return tarjetaDeCredito; }
    public void setTarjetaDeCredito(Banco tarjetaDeCredito) { this.tarjetaDeCredito = tarjetaDeCredito; }   
    public Banco getTarjetaDeDebito() { return tarjetaDeDebito; }
    public void setTarjetaDeDebito(Banco tarjetaDeDebito) { this.tarjetaDeDebito = tarjetaDeDebito; }
    public boolean isEfectivo() { return efectivo; }
    public void setEfectivo(boolean efectivo) { this.efectivo = efectivo; }
    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }

}