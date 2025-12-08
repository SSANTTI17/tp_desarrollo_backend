package com.desarrollo_backend.demo.dtos;

import java.util.List;

public class OcuparDTO{
    private int numeroHabitacion;
    private String tipoHabitacion; // String para facilitar el enum conversion
    private String fechaInicio;    // yyyy-MM-dd
    private String fechaFin;       // yyyy-MM-dd
    private List<HuespedDTO> huespedes; // Lista de huéspedes a ocupar

    // Getters y Setters
    public int getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(int numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }
    public String getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public List<HuespedDTO> getHuespedes() { return huespedes; }
    public void setHuespedes(List<HuespedDTO> huespedes) { this.huespedes = huespedes; }
}