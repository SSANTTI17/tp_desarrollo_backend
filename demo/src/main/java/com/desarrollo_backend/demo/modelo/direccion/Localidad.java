package com.desarrollo_backend.demo.modelo.direccion;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "localidades")
public class Localidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "localidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones;

    @ManyToOne
    @JoinColumn(name = "provincia_nombre") 
    private Provincia provincia;

    public Localidad(){}

    public Localidad(String nombre, Provincia provincia){
        this.nombre = nombre;
        this.provincia = provincia;
        direcciones = new ArrayList<>();
    }

    public String getNombre(){ return nombre; }
}
