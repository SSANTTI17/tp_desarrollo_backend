package com.desarrollo_backend.demo.dtos.requests;
import java.util.List;
import com.desarrollo_backend.demo.dtos.*;

// Clase auxiliar para mapear el body del request de Confirmar Factura
    public class ConfirmarFacturaRequest {
        private Integer idEstadia;
        private FacturaDTO factura;
        private HuespedDTO huesped;
        private PersonaJuridicaDTO responsable; // 'resp' en la descripción
        private List<ConsumoDTO> consumos;

        // Getters y Setters
        public Integer getIdEstadia() { return idEstadia; }
        public void setIdEstadia(Integer idEstadia) { this.idEstadia = idEstadia; }
        public FacturaDTO getFactura() { return factura; }
        public void setFactura(FacturaDTO factura) { this.factura = factura; }
        public HuespedDTO getHuesped() { return huesped; }
        public void setHuesped(HuespedDTO huesped) { this.huesped = huesped; }
        public PersonaJuridicaDTO getResponsable() { return responsable; }
        public void setResponsable(PersonaJuridicaDTO responsable) { this.responsable = responsable; }
        public List<ConsumoDTO> getConsumos() { return consumos; }
        public void setConsumos(List<ConsumoDTO> consumos) { this.consumos = consumos; }
    }