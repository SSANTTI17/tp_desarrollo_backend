package com.desarrollo_backend.demo.modelo.responsablePago;

import jakarta.persistence.*;

@Entity
@Table(name = "personas_juridicas")
public class PersonaJuridica extends ResponsablePago {

    @Column(nullable = false, length = 100)
    private String RazonSocial;

}
