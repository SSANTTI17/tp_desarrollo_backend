package com.desarrollo_backend.demo.modelo.factura;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import com.desarrollo_backend.demo.dtos.NotaDeCreditoDTO;


@Entity
@Table(name = "notas_de_credito")
public class NotaDeCredito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date fechaEmision;
    
    @Column(nullable = false)
    private float monto;

    @OneToMany (mappedBy = "notaDeCredito")
    private List<Factura> facturasAsociada;

    //constructores
    public NotaDeCredito(){}   
    public NotaDeCredito(Date fechaEmision, float monto, List<Factura> facturasAsociada){
        this.fechaEmision = fechaEmision;
        this.monto = monto;
        this.facturasAsociada = facturasAsociada;
    }

    public NotaDeCredito(NotaDeCreditoDTO n){
        this.fechaEmision = n.getFechaEmision();
        this.monto = n.getMonto();
        this.facturasAsociada = n.getFacturasAsociadas();
    }

    //getters y setters
    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }
    public float getMonto() { return monto; }
    public void setMonto(float monto) { this.monto = monto; }
    public List<Factura> getFacturasAsociadas() { return facturasAsociada; }
    public void addFacturaAsociada(Factura facturaAsociada) { this.facturasAsociada.add(facturaAsociada); }
}