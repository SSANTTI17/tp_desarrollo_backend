package com.desarrollo_backend.demo.modelo.factura;

public enum TipoFactura {
    A,
    B;

    public static TipoFactura fromString(String s) {
        if (s == null) return B;
        switch (s.trim().toUpperCase()) {
            case "A": return A;
            case "B": return B;
            default: return B;
        }
    }
}
