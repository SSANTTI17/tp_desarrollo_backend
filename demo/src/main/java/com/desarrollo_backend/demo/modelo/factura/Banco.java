package com.desarrollo_backend.demo.modelo.factura;

public enum Banco {
    BNA,
    Macro,
     Santa_Fe,
    Galicia,
    BBVA,
    Santander,
    HSBC,
    ICBC,
    OTRO;
    
    public static Banco fromString(String s) {
        if (s == null) return OTRO;
        switch (s.trim().toUpperCase()) {
            case "BNA": return BNA;
            case "MACRO": return Macro;
            case "SANTA_FE": return Santa_Fe;
            case "GALICIA": return Galicia;
            case "BBVA": return BBVA;
            case "SANTANDER": return Santander;
            case "HSBC": return HSBC;
            case "ICBC": return ICBC;
            default: return OTRO;
        }
    }
}
