package com.desarrollo_backend.demo.modelo.conserje;

import jakarta.persistence.*; // IMPORTANTE: Esto trae @Entity, @Id, etc.

@Entity
@Table(name = "conserjes")
public class Conserje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Integer id;

    private String nombre;
    private String contrasenia;

    // --- Constructores ---
    public Conserje() {
    }

    // Constructor original (modificado para no pedir ID, ya que se autogenera)
    public Conserje(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    // --- Getters y Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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