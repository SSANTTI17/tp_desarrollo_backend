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

    public FormaDePago() {
    }

    public FormaDePago(FormaDePagoDTO fpDTO) {
        this.tarjetaDeCredito = fpDTO.getTarjetaDeCredito();
        this.tarjetaDeDebito = fpDTO.getTarjetaDeDebito();
        this.efectivo = fpDTO.isEfectivo();
        this.monto = fpDTO.getMonto();
        // La factura se asigna externamente con setFactura
    }

    // --- GETTERS Y SETTERS (ESTOS FALTABAN) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Banco getTarjetaDeCredito() {
        return tarjetaDeCredito;
    }

    public void setTarjetaDeCredito(Banco tarjetaDeCredito) {
        this.tarjetaDeCredito = tarjetaDeCredito;
    }

    public Banco getTarjetaDeDebito() {
        return tarjetaDeDebito;
    }

    public void setTarjetaDeDebito(Banco tarjetaDeDebito) {
        this.tarjetaDeDebito = tarjetaDeDebito;
    }

    public boolean isEfectivo() {
        return efectivo;
    }

    public void setEfectivo(boolean efectivo) {
        this.efectivo = efectivo;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}