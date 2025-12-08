package com.desarrollo_backend.demo.modelo.habitacion;

public enum TipoHabitacion {
    IE,
    DE,
    DS,
    SFP,
    SD,
    OTRO;

    public static TipoHabitacion fromString(String s) {
        if (s == null) return OTRO;
        switch (s.trim().toUpperCase()) {
            case "IE": return IE;
            case "DE": return DE;
            case "DS": return DS;
            case "SFP": return SFP;
            case "SD": return SD;
            default: return OTRO;
        }
    }
}
