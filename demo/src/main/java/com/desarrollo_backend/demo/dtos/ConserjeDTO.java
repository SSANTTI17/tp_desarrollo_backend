package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.conserje.Conserje;

public class ConserjeDTO {
    private int id;
    private String usuario;
    private String contrasenia;

    //constructores
    public ConserjeDTO() {
    }

    public ConserjeDTO(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public ConserjeDTO(Conserje conserje) {
        this.usuario = conserje.getUsuario();
        this.contrasenia = conserje.getContrasenia();
    }

    //getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return usuario;
    }

    public String getcontrasenia() {
        return contrasenia;
    }

    //setters
    public void setId(int id) {this.id = id;}

    public void setNombre(String usuario) {this.usuario = usuario;}

    public void setcontrasenia(String contrasenia) {this.contrasenia = contrasenia;}

}






