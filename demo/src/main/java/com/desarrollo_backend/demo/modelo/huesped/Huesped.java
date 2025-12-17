package com.desarrollo_backend.demo.modelo.huesped;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;

@Entity
@Getter
@Setter
@NoArgsConstructor // Reemplaza al constructor vacío
@AllArgsConstructor // Genera un constructor con todos los campos
@Table(name = "huespedes")
public class Huesped {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @EmbeddedId
    private HuespedPK id;

    private Date fechaDeNacimiento;

    @Column(nullable = false, length = 100)
    private String nacionalidad;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String ocupacion;

    @Column(nullable = false)
    private boolean alojado;

    @ManyToMany(mappedBy = "huespedes")
    private List<Estadia> estadias;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Boolean borradoLogico = false; // Inicialización por defecto con Lombok

    // Delegados para acceder a los campos de la PK más fácilmente
    public TipoDoc getTipo_documento() {
        return id != null ? id.getTipo_documento() : null;
    }

    public String getNroDocumento() {
        return id != null ? id.getNroDocumento() : null;
    }

    public void setDocumento(TipoDoc tipo, String nro) {
        this.id = new HuespedPK(tipo, nro);
    }

    // --- LÓGICA DE NEGOCIO ---

    public int calcularEdad() {
        if (this.fechaDeNacimiento == null)
            return 0;
        LocalDate nacimiento = this.fechaDeNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ahora = LocalDate.now();
        return Period.between(nacimiento, ahora).getYears();
    }
}