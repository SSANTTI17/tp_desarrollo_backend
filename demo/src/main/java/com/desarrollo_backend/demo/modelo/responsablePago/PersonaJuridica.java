package com.desarrollo_backend.demo.modelo.responsablePago;
import com.desarrollo_backend.demo.dtos.PersonaJuridicaDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "personas_juridicas")
public class PersonaJuridica extends ResponsablePago {

    @Column(nullable = false, length = 100)
    private String RazonSocial;
    @Column(nullable = true)
    private int Telefono;
    @Column(nullable = true)
    private String direccion;

    //constructores
    public PersonaJuridica() {
        super();
    }
    public PersonaJuridica(String CUIT, String razonSocial, int telefono, String direccion) {
        super(CUIT);
        this.RazonSocial = razonSocial;
        this.Telefono = telefono;
        this.direccion = direccion;
    }
    public PersonaJuridica(PersonaJuridicaDTO dto) {
        super(dto.getCUIT());
        this.RazonSocial = dto.getRazonSocial();
        this.Telefono = dto.getTelefono();
        this.direccion = dto.getDireccion();
    }
    //getters y setters
    public String getRazonSocial() {
        return RazonSocial;
    }
    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
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
