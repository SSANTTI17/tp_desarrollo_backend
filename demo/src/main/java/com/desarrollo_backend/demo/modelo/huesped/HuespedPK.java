package com.desarrollo_backend.demo.modelo.huesped;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
public class HuespedPK implements Serializable {

    @Enumerated(EnumType.STRING) // hace que el tipo se guarde como string :D
    @Column(nullable = false)
    private TipoDoc tipo_documento;

    @Column(nullable = false, length = 10)
    private String nroDocumento;

    public HuespedPK() {
    }

    public HuespedPK(TipoDoc tipo_documento, String nroDocumento) {
        this.tipo_documento = tipo_documento;
        this.nroDocumento = nroDocumento;
    }

    // Getters y setters (opcional)
    public TipoDoc getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(TipoDoc tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HuespedPK))
            return false;
        HuespedPK that = (HuespedPK) o;
        return Objects.equals(tipo_documento, that.tipo_documento) && Objects.equals(nroDocumento, that.nroDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo_documento, nroDocumento);
    }
}
