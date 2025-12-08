package com.desarrollo_backend.demo.dtos;

import java.util.Date;

public class ReservaDTO {
    private String nombre;
    private String apellido;
    private int telefono;
    private Date fechaIngreso;
    private String HoraIngreso;
    private Date fechaEgreso;
    private String HoraEgreso;

    public ReservaDTO(String nombre, String apellido, int telefono, Date fechaIngreso, String horaIngreso,
            Date fechaEgreso, String horaEgreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        HoraIngreso = horaIngreso;
        this.fechaEgreso = fechaEgreso;
        HoraEgreso = horaEgreso;
    }

    // getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getTelefono() {
        return telefono;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public String getHoraIngreso() {
        return HoraIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public String getHoraEgreso() {
        return HoraEgreso;
    }

    // setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        HoraIngreso = horaIngreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public void setHoraEgreso(String horaEgreso) {
        HoraEgreso = horaEgreso;
    }
}