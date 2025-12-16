package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import com.desarrollo_backend.demo.modelo.factura.TipoFactura;
import com.desarrollo_backend.demo.modelo.factura.Factura;

public class FacturaDTO {

    private TipoFactura tipoFactura;
    private float valorEstadia;
    private float totalAPagar;
    private float vuelto;
    private boolean pagado;
    private ResponsablePago responsablePago;

    // constructores
    public FacturaDTO() {
    }

    public FacturaDTO(Factura factura) {
        this.tipoFactura = factura.getTipoFactura();
        this.valorEstadia = factura.getValorEstadia();
        this.totalAPagar = factura.getTotalAPagar();
        this.vuelto = factura.getVuelto();
        this.pagado = factura.getPagado();
        this.responsablePago = factura.getResponsablePago();
    }

    // getters y setters
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

}
