package com.desarrollo_backend.demo.dtos;


import java.util.List;


import com.desarrollo_backend.demo.modelo.habitacion.*;

public class HabitacionDTO {

    private TipoHabitacion tipo;
    private int numero;
    private float costoNoche;
    private HistorialEstadoHabitacionDTO historialEstados; 
    private List<ReservaDTO> reservas; // Cambié el nombre a singular para coincidir con el tipo, y ahora es DTO
    private List<EstadoHabitacion> estadosPorDia; // Ahora es una lista de DTOs
    public HabitacionDTO() {}
    
   public HabitacionDTO(Habitacion h) {
        if (h != null) {
            this.tipo = h.getTipo();
            this.numero = h.getNumero();
            this.costoNoche = h.getCostoNoche();
        }
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
    public List<ReservaDTO> getReservas() {
        return reservas;
    }

    public void setReservas(List<ReservaDTO> reservas) {
        this.reservas = reservas;
    }
}
