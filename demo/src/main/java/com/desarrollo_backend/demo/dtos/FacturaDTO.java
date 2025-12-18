package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaJuridica;
import com.desarrollo_backend.demo.modelo.responsablePago.ResponsablePago;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.desarrollo_backend.demo.modelo.factura.TipoFactura;
import com.desarrollo_backend.demo.modelo.factura.Factura;

public class FacturaDTO {

    private TipoFactura tipoFactura;
    private float valorEstadia;
    private float totalAPagar;
    private float vuelto;
    private boolean pagado;
    private String nombreResponsable;
    @JsonIgnore // rompe bucle infinito
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

        if (factura.getResponsablePago() != null) {
            ResponsablePago resp = factura.getResponsablePago();

            if (resp instanceof PersonaFisica pf) {
                // Si es persona física, sacamos nombre del huésped asociado
                if (pf.getHuesped() != null) {
                    this.nombreResponsable = pf.getHuesped().getApellido() + ", " + pf.getHuesped().getNombre();
                }
            } else if (resp instanceof PersonaJuridica pj) {
                // Si es jurídica, usamos la razón social
                this.nombreResponsable = pj.getRazonSocial();
            }
        }

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

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public ResponsablePago getResponsablePago() {
        return responsablePago;
    }

    public void setResponsablePago(ResponsablePago p) {
        this.responsablePago = p;
    }

}
