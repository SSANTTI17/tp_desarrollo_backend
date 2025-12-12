package com.desarrollo_backend.demo.excepciones;

public class ReservaNotFoundExc extends Exception {
   
    public ReservaNotFoundExc() {
        super();
    }

    public ReservaNotFoundExc(String message) {
        super(message);
    }
}
