package com.desarrollo_backend.demo.dtos;

import java.util.Date;

import com.desarrollo_backend.demo.modelo.factura.Banco;

public class ChequeDTO {
    private int nro;
    private Banco banco;
    private String plaza;
    private float monto;
    private Date fechaCobro;

    //constructores
    public ChequeDTO(int nro, Banco banco, String plaza, float monto, Date fechaCobro){
        this.nro = nro;
        this.banco = banco;
        this.plaza = plaza;
        this.monto = monto;
        this.fechaCobro = fechaCobro;
    }

    //getter
    public int getNro() { return nro; }
    public Banco getBanco() { return banco; }
    public String getPlaza() { return plaza; }
    public float getMonto() { return monto; }
    public Date getFechaCobro() { return fechaCobro; }

    //setter
    public void setNro(int nro) { this.nro = nro; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setPlaza(String plaza) { this.plaza = plaza; }
    public void setMonto(float monto) { this.monto = monto; }
    public void setFechaCobro(Date fechaCobro) { this.fechaCobro = fechaCobro; }
}


