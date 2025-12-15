package com.desarrollo_backend.demo.modelo.huesped;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String direccion;

    public Huesped() {
    }

    // Constructor completo actualizado con dirección
    public Huesped(String nombre, String apellido, TipoDoc tipo_documento, String nroDocumento,
            Date fechaDeNacimiento, String nacionalidad, String email,
            String telefono, String ocupacion, boolean alojado, String direccion) {
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
    }

    // Constructor desde DTO actualizado
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
        // CORRECCIÓN: Asignar la dirección desde el DTO
        this.direccion = huespedDto.getDireccion() != null ? huespedDto.getDireccion() : "Sin dirección";
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

    public String getDireccion() {
        return direccion;
    }

    public void setAlojado(boolean alojado) {
        this.alojado = alojado;
    }

    public void setDireccion(String direccion) {
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
        // CORRECCIÓN: Actualizar dirección también
        if (huespedDto.getDireccion() != null) {
            this.direccion = huespedDto.getDireccion();
        }
    }

    public int calcularEdad() {
        if (this.fechaDeNacimiento == null)
            return 0;
        LocalDate nacimiento = this.fechaDeNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ahora = LocalDate.now();
        return Period.between(nacimiento, ahora).getYears();
    }
}