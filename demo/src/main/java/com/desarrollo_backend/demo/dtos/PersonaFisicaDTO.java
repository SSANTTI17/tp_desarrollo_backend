package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;

public class PersonaFisicaDTO {

    private String CUIT;
    private String PosicionIVA;
    private HuespedDTO RefHuesped;

    public PersonaFisicaDTO(String CUIT, String PosicionIVA, HuespedDTO huesped) {
        this.CUIT = CUIT;
        this.PosicionIVA = PosicionIVA;
        this.RefHuesped = huesped;
    }

    public PersonaFisicaDTO() {
    }

    public PersonaFisicaDTO(PersonaFisica p) {
        this.CUIT = p.getCUIT();
        this.PosicionIVA = p.getPosicionIVA();

        // CORRECCIÓN: Mapeo manual porque el constructor HuespedDTO(Huesped) ya no
        // existe
        if (p.getHuesped() != null) {
            Huesped h = p.getHuesped();
            HuespedDTO hDto = new HuespedDTO();
            hDto.setNombre(h.getNombre());
            hDto.setApellido(h.getApellido());
            hDto.setTipo_documento(h.getTipo_documento());
            hDto.setNroDocumento(h.getNroDocumento());
            hDto.setFechaDeNacimiento(h.getFechaDeNacimiento());
            hDto.setNacionalidad(h.getNacionalidad());
            hDto.setEmail(h.getEmail());
            hDto.setTelefono(h.getTelefono());
            hDto.setOcupacion(h.getOcupacion());
            hDto.setAlojado(h.isAlojado());
            hDto.setDireccion(h.getDireccion());
            // No seteamos CUIT/IVA en el DTO interno del huesped para evitar redundancia
            // cíclica
            this.RefHuesped = hDto;
        }
    }

    public PersonaFisicaDTO(HuespedDTO huesped) {
        this.CUIT = "";
        this.PosicionIVA = "";
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

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public void setPosicionIVA(String PosicionIVA) {
        this.PosicionIVA = PosicionIVA;
    }

    public void setHuesped(HuespedDTO h) {
        RefHuesped = h;
    }
}