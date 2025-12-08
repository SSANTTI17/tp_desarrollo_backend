package com.desarrollo_backend.demo.modelo.cheque;

import jakarta.persistence.*;
import com.desarrollo_backend.demo.modelo.factura.Banco;
import java.util.Date;

@Entity
@Table(name = "cheques")
public class Cheque {
     
    @Id
    private int id;

    @Column(nullable = false)
    private int nro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private Banco banco;
    
    @Column(nullable = false, length = 100)
    private String plaza;
    
    @Column(nullable = false)
    private float monto;
    
    @Column(nullable = false)
    private Date fechaCobro;

    //constructores
    public Cheque(){}
    public Cheque(int nro, Banco banco, String plaza, float monto, Date fechaCobro){
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
