package com.desarrollo_backend.demo.dtos;

import java.util.List;
import com.desarrollo_backend.demo.modelo.habitacion.*;

public class HabitacionDTO {

    private TipoHabitacion tipo;
    private int numero;
    private float costoNoche;
    private HistorialEstadoHabitacionDTO historialEstados;
    private Reserva reservas;
    private List<EstadoHabitacion> estadosPorDia;

    public HabitacionDTO() {}
    
    public HabitacionDTO(Habitacion h){
        this.tipo = h.getTipo();
        this.numero = h.getNumero();
        this.costoNoche = h.getCostoNoche();
    }

    // Getter y Setter para tipo
    public TipoHabitacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoHabitacion tipo) {
        this.tipo = tipo;
    }

    // Getter y Setter para numero
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    // Getter y Setter para costoNoche
    public float getCostoNoche() {
        return costoNoche;
    }

    public void setCostoNoche(float costoNoche) {
        this.costoNoche = costoNoche;
    }

    // Getter y Setter para historialEstados
    public HistorialEstadoHabitacionDTO getHistorialEstados() {
        return historialEstados;
    }

    // Getter y Setter para Estados
    public List<EstadoHabitacion> getEstadosPorDia() { return estadosPorDia; }
    public void setEstadosPorDia(List<EstadoHabitacion> estadosPorDia) { this.estadosPorDia = estadosPorDia; }

    public void setHistorialEstados(HistorialEstadoHabitacionDTO historialEstados) {
        this.historialEstados = historialEstados;
    }

    // Getter y Setter para reservas
    public Reserva getReservas() {
        return reservas;
    }

    public void setReservas(Reserva reservas) {
        this.reservas = reservas;
    }
}
