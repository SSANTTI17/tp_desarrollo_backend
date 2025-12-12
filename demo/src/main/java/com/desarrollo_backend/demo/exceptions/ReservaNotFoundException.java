package com.desarrollo_backend.demo.exceptions;

public class ReservaNotFoundException extends Exception {
   
    public ReservaNotFoundException() {
        super();
    }

    public ReservaNotFoundException(String message) {
        super(message);
    }
}
