package com.desarrollo_backend.demo.modelo.estadias;

public enum Moneda {
    ARS,
 USD,
 UYU,
 EUR;

    public static Moneda fromString(String s) {
        if (s == null) return ARS;
        switch (s.trim().toUpperCase()) {
            case "ARS": return ARS;
            case "USD": return USD;
            case "UYU": return UYU;
            case "EUR": return EUR;
            default: return ARS;
        }
    }
}
