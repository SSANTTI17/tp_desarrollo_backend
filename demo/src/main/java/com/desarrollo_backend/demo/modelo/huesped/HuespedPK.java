package com.desarrollo_backend.demo.modelo.huesped;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data // Genera getters, setters, toString, y lo más importante: equals y hashCode
@NoArgsConstructor
@AllArgsConstructor
public class HuespedPK implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDoc tipo_documento;

    @Column(nullable = false, length = 10)
    private String nroDocumento;
}