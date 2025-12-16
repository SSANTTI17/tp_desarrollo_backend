package com.desarrollo_backend.demo.dtos;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaJuridica;

public class PersonaJuridicaDTO {
    private String razonSocial;
    private String CUIT;
    private int Telefono;
    private String direccion;
    //constructores
    public PersonaJuridicaDTO() {}
    public PersonaJuridicaDTO(String razonSocial, String CUIT, int telefono, String direccion) {
        this.razonSocial = razonSocial;
        this.CUIT = CUIT;
        this.Telefono = telefono;
        this.direccion = direccion;
    }
    public PersonaJuridicaDTO(PersonaJuridica personaJuridica) {
        this.razonSocial = personaJuridica.getRazonSocial();
        this.CUIT = personaJuridica.getCUIT();
        this.Telefono = personaJuridica.getTelefono();
        this.direccion = personaJuridica.getDireccion();
    }
    //getters y setters
    public String getRazonSocial() {
        return razonSocial;
    }
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public String getCUIT() {
        return CUIT;
    }
    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }
    public int getTelefono() {
        return Telefono;
    }
    public void setTelefono(int telefono) {
        Telefono = telefono;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
