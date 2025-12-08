package com.desarrollo_backend.demo.dtos;

import java.util.List;
import java.util.ArrayList;
import com.desarrollo_backend.demo.modelo.direccion.Provincia;

public class PaisDTO {

    private String nombre;

    private List<Provincia> provincias;

    public PaisDTO() {
    }

    public PaisDTO(String nombre) {
        this.nombre = nombre;
        provincias = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

}