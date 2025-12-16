package com.desarrollo_backend.demo.modelo.factura;

import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "facturas")
public class Factura {

    // pk para la persistencia
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoFactura tipoFactura;

    @Column(nullable = false)
    private float valorEstadia;

    @Column(nullable = false)
    private float totalAPagar;

    @Column(nullable = false)
    private float vuelto;

    @Column(nullable = false)
    private boolean pagado;

    @ManyToOne
    @JoinColumn(name = "responsable_pago")
    private ResponsablePago responsablePago;

    @OneToMany(mappedBy = "factura")
    private List<FormaDePago> formasDePago;

    @ManyToOne
    @JoinColumn(name = "nota_cancela")
    private NotaDeCredito notaDeCredito;

    public Factura() {
    }

    // creo q esto es lo mínimo idkkk
    public Factura(ResponsablePago responsablePago, int valorEstadia, float totalAPagar) {
        this.responsablePago = responsablePago;
        this.totalAPagar = totalAPagar;
        this.valorEstadia = valorEstadia;
        formasDePago = new ArrayList<>();
    }

    public Factura(FacturaDTO FacturaDTO){
        this.tipoFactura = FacturaDTO.getTipoFactura();
        this.valorEstadia = FacturaDTO.getValorEstadia();   
        this.totalAPagar = FacturaDTO.getTotalAPagar();
        this.vuelto = FacturaDTO.getVuelto();
        this.pagado = FacturaDTO.getPagado();
        this.responsablePago = FacturaDTO.getResponsablePago();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoFactura getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public float getValorEstadia() {
        return valorEstadia;
    }

    public void setValorEstadia(float valorEstadia) {
        this.valorEstadia = valorEstadia;
    }

    public float getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(float totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public float getVuelto() {
        return vuelto;
    }

    public void setVuelto(float vuelto) {
        this.vuelto = vuelto;
    }

    public boolean getPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public ResponsablePago getResponsablePago() {
        return responsablePago;
    }

    public void setResponsablePago(ResponsablePago p) {
        this.responsablePago = p;
    }

    public List<FormaDePago> getFormasDePago() {
        return formasDePago;
    }

    public void setFormasDePago(List<FormaDePago> formasDePago) {
        this.formasDePago = formasDePago;
    }

    public void agregarFormaDePago(FormaDePago f) {
        formasDePago.add(f);
    }

}
