package com.desarrollo_backend.demo.modelo.huesped;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import com.desarrollo_backend.demo.modelo.direccion.Direccion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.dtos.HuespedDTO;

@Entity
@Table(name = "huespedes")
public class Huesped {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @EmbeddedId
    private HuespedPK id; // id tipo y nro de documento

    private Date fechaDeNacimiento;

    @Column(nullable = false, length = 100)
    private String nacionalidad;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String ocupacion;

    @Column(nullable = false)
    private boolean alojado;

    @OneToMany(mappedBy = "huespedRef")
    private List<Reserva> reservasAsociadas;

    @ManyToOne
    @JoinColumn(name = "direccion_huesped")
    private Direccion direccion;

    public Huesped() {
    }

    public Huesped(String nombre, String apellido, TipoDoc tipo_documento, String nroDocumento,
            Date fechaDeNacimiento, String nacionalidad, String email,
            String telefono, String ocupacion, boolean alojado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = new HuespedPK(tipo_documento, nroDocumento);
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.nacionalidad = nacionalidad;
        this.email = email;
        this.telefono = telefono;
        this.ocupacion = ocupacion;
        this.alojado = alojado;
    }

    public Huesped(HuespedDTO huespedDto) {
        this.nombre = huespedDto.getNombre();
        this.apellido = huespedDto.getApellido();
        this.id = new HuespedPK(huespedDto.getTipo_documento(), huespedDto.getNroDocumento());
        this.fechaDeNacimiento = huespedDto.getFechaDeNacimiento();
        this.nacionalidad = huespedDto.getNacionalidad();
        this.email = huespedDto.getEmail();
        this.telefono = huespedDto.getTelefono();
        this.ocupacion = huespedDto.getOcupacion();
        this.alojado = huespedDto.isAlojado();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public TipoDoc getTipo_documento() {
        return id.getTipo_documento();
    }

    public String getNroDocumento() {
        return id.getNroDocumento();
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public boolean isAlojado() {
        return alojado;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setAlojado(boolean alojado) {
        this.alojado = alojado;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setHuesped(HuespedDTO huespedDto) {
        this.nombre = huespedDto.getNombre();
        this.apellido = huespedDto.getApellido();
        this.id = new HuespedPK(huespedDto.getTipo_documento(), huespedDto.getNroDocumento());
        this.fechaDeNacimiento = huespedDto.getFechaDeNacimiento();
        this.nacionalidad = huespedDto.getNacionalidad();
        this.email = huespedDto.getEmail();
        this.telefono = huespedDto.getTelefono();
        this.ocupacion = huespedDto.getOcupacion();
        //alojado solo se modifica al pagar
    }
}