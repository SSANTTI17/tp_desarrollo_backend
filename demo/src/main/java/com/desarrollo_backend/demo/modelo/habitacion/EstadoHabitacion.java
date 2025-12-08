package com.desarrollo_backend.demo.modelo.habitacion;

public enum EstadoHabitacion {
    
    Ocupada,
    Reservada,
    FueraDeServicio,
    Disponible; //solo para el default pero no deberia usarse?

    public static EstadoHabitacion fromString(String s) {
        if (s == null) return Disponible;
        switch (s.trim().toUpperCase()) {
            case "Ocupada": return Ocupada;
            case "Reservada": return Reservada;
            case "FueraDeServicio": return FueraDeServicio;
            default: return Disponible;
        }
    }

}
