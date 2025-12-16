package com.desarrollo_backend.demo.modelo.conserje;

public class Conserje {

    private String nombre;

    private String contrasenia;

    public Conserje() {
    }

    public Conserje(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}