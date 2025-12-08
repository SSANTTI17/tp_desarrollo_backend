package com.desarrollo_backend.demo.modelo.direccion;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provincias")
public class Provincia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    // Muchas provincias pertenecen a un mismo país
    @ManyToOne(optional = false)
    @JoinColumn(name = "pais_id")
    private Pais pais;

    // Una provincia tiene muchas localidades
    @OneToMany(mappedBy = "provincia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Localidad> localidades = new ArrayList<>();

    public Provincia() {}

    public Provincia(String nombre, Pais pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Pais getPais() { return pais; }
    public void setPais(Pais pais) { this.pais = pais; }

    public List<Localidad> getLocalidades() { return localidades; }
    public void setLocalidades(List<Localidad> localidades) { this.localidades = localidades; }
}
