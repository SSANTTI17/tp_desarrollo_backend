package com.desarrollo_backend.demo.modelo.responsablePago;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ResponsablePago {
    
    @Id
    private String CUIT;

    protected ResponsablePago() {}

    public ResponsablePago(String CUIT) {
        this.CUIT = CUIT;
    }

    public String getCUIT() {
        return CUIT;
    }
    

}