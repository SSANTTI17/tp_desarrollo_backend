package com.desarrollo_backend.demo.modelo.factura;

import jakarta.persistence.*;
import com.desarrollo_backend.demo.dtos.FormaDePagoDTO;

@Entity
@Table(name = "formas_de_pago")
public class FormaDePago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Banco tarjetaDeCredito;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Banco tarjetaDeDebito;    

    @Column(nullable = true)
    private boolean efectivo;

    @Column(nullable = false)
    private float monto;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    public FormaDePago(){}

    //lo hago aca pq no se usa en ningún lado
    public boolean cargarFormaDePago(Banco credito, Banco debito, boolean efectivo, float monto){
        
        if(credito == null && debito == null && efectivo){
            this.efectivo = true;
            this.monto = monto;
            tarjetaDeCredito = tarjetaDeDebito = null;
            return true;
        }

        if(credito != null && debito == null && !efectivo){
            this.efectivo = false;
            this.monto = monto;
            tarjetaDeCredito = credito;
            tarjetaDeDebito = null;
            return true;
        }

        if(credito == null && debito != null && !efectivo){
            this.efectivo = false;
            this.monto = monto;
            tarjetaDeCredito = null;
            tarjetaDeDebito = debito;
            return true;
        }

        tarjetaDeCredito = tarjetaDeDebito = null;
        this.efectivo = false;
        this.monto = monto;

        return false;

    }  

    public FormaDePago(FormaDePagoDTO fpDTO){
        this.tarjetaDeCredito = fpDTO.getTarjetaDeCredito();
        this.tarjetaDeDebito = fpDTO.getTarjetaDeDebito();
        this.efectivo = fpDTO.isEfectivo();
        this.monto = fpDTO.getMonto();
        this.factura = fpDTO.getFactura();
    }

}
