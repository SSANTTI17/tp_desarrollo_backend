package com.desarrollo_backend.demo.dtos;

public class ContenedorDeAltaHuesped {

    private HuespedDTO huesped;
    private PersonaFisicaDTO personaFisica; // Puede venir null si no cargan CUIT

    public ContenedorDeAltaHuesped() {
    }

    public HuespedDTO getHuesped() {
        return huesped;
    }

    public void setHuesped(HuespedDTO huesped) {
        this.huesped = huesped;
    }

    public PersonaFisicaDTO getPersonaFisica() {
        return personaFisica;
    }

    public void setPersonaFisica(PersonaFisicaDTO personaFisica) {
        this.personaFisica = personaFisica;
    }
}