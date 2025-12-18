package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.factura.Factura;

public class ContenedorEstadiaYFacturaDTO {
    private EstadiaDTO estadia;
    private FacturaDTO factura;

    // constructores
    public ContenedorEstadiaYFacturaDTO(EstadiaDTO estadia, FacturaDTO factura) {
        this.estadia = estadia;
        this.factura = factura;
    }

    public ContenedorEstadiaYFacturaDTO(Estadia estadia, Factura factura) {
        this.estadia = new EstadiaDTO(estadia);
        
        this.factura = new FacturaDTO(factura);
        
    }

    // getters
    public EstadiaDTO getEstadia() {
        return estadia;
    }

    public FacturaDTO getFactura() {
        return factura;
    }
}
