package com.desarrollo_backend.demo.modelo.huesped;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.dtos.HuespedDTO;

@Entity
@Table(name = "huespedes")
public class Huesped {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @EmbeddedId
    private HuespedPK id;

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

    @ManyToMany(mappedBy = "huespedes")
    private List<Estadia> estadias;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Boolean borradoLogico;

    public Huesped() {
        this.borradoLogico = false;
        this.direccion = "Sin dirección";
        this.alojado = false;
        this.nacionalidad = "Argentina";
        this.telefono = "000000";
        this.ocupacion = "Ninguna";
        this.apellido = "Test";
    }

    // Constructor completo
    public Huesped(String nombre, String apellido, TipoDoc tipo_documento, String nroDocumento,
            Date fechaDeNacimiento, String nacionalidad, String email,
            String telefono, String ocupacion, boolean alojado, String direccion, boolean borradoLogico) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = new HuespedPK(tipo_documento, nroDocumento);
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.nacionalidad = nacionalidad;
        this.email = email;
        this.telefono = telefono;
        this.ocupacion = ocupacion;
        this.alojado = alojado;
        this.direccion = direccion;
        this.borradoLogico = borradoLogico;
    }

    // Constructor desde DTO
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
        this.direccion = huespedDto.getDireccion() != null ? huespedDto.getDireccion() : "Sin dirección";
        this.borradoLogico = huespedDto.getBorrado();
    }

    // --- GETTERS ---

    public List<Estadia> getEstadias() {
        return estadias;
    }

    public HuespedPK getId() {
        return id;
    }

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

    public String getDireccion() {
        return direccion;
    }

    public Boolean getBorrado() {
        return borradoLogico;
    }

    // --- SETTERS ---
    public void setEstadias(List<Estadia> estadias) {
        this.estadias = estadias;
    }

    public void setBorradoLogico(Boolean borradoLogico) {
        this.borradoLogico = borradoLogico;
    }

    public void setAlojado(boolean alojado) {
        this.alojado = alojado;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDocumento(TipoDoc tipo_documento, String nroDocumento) {
        this.id = new HuespedPK(tipo_documento, nroDocumento);
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
        if (huespedDto.getDireccion() != null) {
            this.direccion = huespedDto.getDireccion();
        }
        this.borradoLogico = huespedDto.getBorrado();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    // --- LÓGICA DE NEGOCIO ---
    public int calcularEdad() {
        if (this.fechaDeNacimiento == null)
            return 0;
        LocalDate nacimiento = this.fechaDeNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ahora = LocalDate.now();
        return Period.between(nacimiento, ahora).getYears();
    }
}