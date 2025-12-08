package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;

public class PersonaFisicaDTO {
    
    private String CUIT;
    private String PosicionIVA;
    private HuespedDTO RefHuesped;

    public PersonaFisicaDTO(String CUIT, String PosicionIVA, HuespedDTO huesped){
        this.CUIT = CUIT;
        this.PosicionIVA = PosicionIVA;
        this.RefHuesped = huesped;
    }

    public PersonaFisicaDTO() {
    }

    public PersonaFisicaDTO(PersonaFisica p){

        CUIT = p.getCUIT();
        PosicionIVA = p.getPosicionIVA();
        RefHuesped = new HuespedDTO(p.getHuesped());
    }

    public PersonaFisicaDTO(HuespedDTO huesped){
        this.CUIT = "";
        this.PosicionIVA="";
        this.RefHuesped = huesped;
    }
    public String getPosicionIVA() {
        return PosicionIVA;
    }

    public HuespedDTO getHuesped() {
        return RefHuesped;
    }

    public String getCUIT() {
        return CUIT;
    }

    public void setCUIT(String CUIT){
        this.CUIT = CUIT;    
    }
    
    public void setPosicionIVA(String PosicionIVA){
        this.PosicionIVA = PosicionIVA;
    }

    public void setHuesped(HuespedDTO h){
        RefHuesped = h;
    }

}
