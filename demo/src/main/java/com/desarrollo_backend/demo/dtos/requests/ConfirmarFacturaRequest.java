package com.desarrollo_backend.demo.dtos.requests;

import java.util.List;
import com.desarrollo_backend.demo.dtos.*;

public class ConfirmarFacturaRequest {
    private Integer idEstadia;
    private FacturaDTO factura;
    private HuespedDTO huesped;
    private PersonaJuridicaDTO responsable;
    private List<ConsumoDTO> consumos;
    private List<FormaDePagoDTO> formasPago; // <--- NUEVO CAMPO

    // Getters y Setters
    public Integer getIdEstadia() {
        return idEstadia;
    }

    public void setIdEstadia(Integer idEstadia) {
        this.idEstadia = idEstadia;
    }

    public FacturaDTO getFactura() {
        return factura;
    }

    public void setFactura(FacturaDTO factura) {
        this.factura = factura;
    }

    public HuespedDTO getHuesped() {
        return huesped;
    }

    public void setHuesped(HuespedDTO huesped) {
        this.huesped = huesped;
    }

    public PersonaJuridicaDTO getResponsable() {
        return responsable;
    }

    public void setResponsable(PersonaJuridicaDTO responsable) {
        this.responsable = responsable;
    }

    public List<ConsumoDTO> getConsumos() {
        return consumos;
    }

    public void setConsumos(List<ConsumoDTO> consumos) {
        this.consumos = consumos;
    }

    // Nuevo Getter y Setter
    public List<FormaDePagoDTO> getFormasPago() {
        return formasPago;
    }

    public void setFormasPago(List<FormaDePagoDTO> formasPago) {
        this.formasPago = formasPago;
    }
}