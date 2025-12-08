package com.desarrollo_backend.demo.modelo.huesped;

public enum TipoDoc {
    DNI,
    LE,
    LC,
    PASAPORTE,
    OTRO;

    public static TipoDoc fromString(String s) {
        if (s == null) return OTRO;
        switch (s.trim().toUpperCase()) {
            case "DNI": return DNI;
            case "LE": return LE;
            case "LC": return LC;
            case "PASAPORTE": return PASAPORTE;
            default: return OTRO;
        }
    }


}
