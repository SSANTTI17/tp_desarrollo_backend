package com.desarrollo_backend.demo.modelo.estadias;

public enum TipoConsumo {
    
    Lavado,
    Sauna,
    Bar,
    Otro;

    public static TipoConsumo fromString(String s) {
        if (s == null) return Otro;
        switch (s.trim().toUpperCase()) {
            case "Lavado": return Lavado;
            case "Sauna": return Sauna;
            case "Bar": return Bar;
            default: return Otro;
        }
    }
}
