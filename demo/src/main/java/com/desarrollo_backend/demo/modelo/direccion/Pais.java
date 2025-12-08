package com.desarrollo_backend.demo.modelo.direccion;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "paises")
public class Pais {

    @Id
    private String nombre;

    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provincia> provincias;

    public Pais(){}

    public Pais(String nombre){
        this.nombre = nombre;
        provincias = new ArrayList<>();
    }

    public String getNombre(){ return nombre; }


}