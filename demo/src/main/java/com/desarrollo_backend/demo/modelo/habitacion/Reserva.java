package com.desarrollo_backend.demo.modelo.habitacion;

import java.util.Date;
import java.util.List;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellido;
    private String telefono;
    private Date fechaIngreso;

    // Atributos en minúscula (Estándar Java)
    private String horaIngreso;
    private Date fechaEgreso;
    private String horaEgreso;

    @ManyToMany
    @JoinTable(name = "habitaciones_reservadas", joinColumns = @JoinColumn(name = "reserva_id"), inverseJoinColumns = {
            @JoinColumn(name = "habitacion_numero", referencedColumnName = "numero"),
            @JoinColumn(name = "habitacion_tipo", referencedColumnName = "tipo")
    })
    @JsonIgnoreProperties("reservas") // Evita bucle infinito al enviar al front
    private List<Habitacion> habitacionesReservadas;

    @OneToOne(mappedBy = "reserva")
    private Estadia estadia;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "huesped_tipo_doc", referencedColumnName = "tipo_documento"),
            @JoinColumn(name = "huesped_numero_doc", referencedColumnName = "nroDocumento")
    })
    Huesped huespedRef;

    public Reserva() {
    }



    // --- GETTERS Y SETTERS CORREGIDOS (CamelCase) ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // CORREGIDO: De gethoraIngreso a getHoraIngreso
    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    // CORREGIDO: De gethoraEgreso a getHoraEgreso
    public String getHoraEgreso() {
        return horaEgreso;
    }

    public void setHoraEgreso(String horaEgreso) {
        this.horaEgreso = horaEgreso;
    }

    public List<Habitacion> getHabitacionesReservadas() {
        return habitacionesReservadas;
    }

    public void setHabitacionesReservadas(List<Habitacion> habitacionesReservadas) {
        this.habitacionesReservadas = habitacionesReservadas;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public void setEstadia(Estadia estadia) {
        this.estadia = estadia;
    }

    public Huesped getHuespedRef() {
        return huespedRef;
    }

    public void setHuespedRef(Huesped huespedRef) {
        this.huespedRef = huespedRef;
    }
}