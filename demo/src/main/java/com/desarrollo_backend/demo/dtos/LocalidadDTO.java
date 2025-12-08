package com.desarrollo_backend.demo.dtos;

import java.util.List;
import java.util.ArrayList;

public class LocalidadDTO {
    
    private String nombre;
    private List<DireccionDTO> direcciones;
    private ProvinciaDTO provincia;

    public LocalidadDTO(){}

    public LocalidadDTO(String nombre, ProvinciaDTO provincia){
        this.nombre = nombre;
        this.provincia = provincia;
        direcciones = new ArrayList<>();
    }

    public String getNombre(){ return nombre; }
    public List<DireccionDTO> getDirecciones(){ return direcciones; }
    public ProvinciaDTO getProvincia(){ return provincia; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public void setDirecciones(List<DireccionDTO> direcciones){ this.direcciones = direcciones; }
    public void setProvincia(ProvinciaDTO provincia){ this.provincia = provincia; }
}
