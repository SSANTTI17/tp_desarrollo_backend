package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.direccion.Localidad;
import java.util.List;
import java.util.ArrayList;

public class DireccionDTO {

    private int codigoPostal;
    private String calle;
    private String departamento;
    private int altura;
    private int piso;
    private Localidad localidad;
    private List<Huesped> huespedes;

    //constructores
    public DireccionDTO(int codigoPostal, String calle, String departamento, int altura, int piso, Localidad localidad){
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.departamento = departamento;
        this.altura = altura;
        this.piso = piso;
        this.localidad = localidad;
        this.huespedes = new ArrayList<>();
    }  

    //getter
    public int getCodigoPostal() { return codigoPostal; }
    public String getCalle() { return calle; }
    public String getDepartamento() { return departamento; }
    public int getAltura() { return altura; }
    public int getPiso() { return piso; }
    public Localidad getLocalidad() { return localidad; }
    public List<Huesped> getHuespedes() { return huespedes; }

    //setter
    public void setCodigoPostal(int codigoPostal) { this.codigoPostal = codigoPostal; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setAltura(int altura) { this.altura = altura; }
    public void setPiso(int piso) { this.piso = piso; }
    public void setLocalidad(Localidad localidad) { this.localidad = localidad; }
    public void setHuespedes(List<Huesped> huespedes) { this.huespedes = huespedes; }

}