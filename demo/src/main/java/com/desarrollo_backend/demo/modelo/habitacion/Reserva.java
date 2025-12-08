package com.desarrollo_backend.demo.modelo.habitacion;

import java.util.Date;
import java.util.List;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import jakarta.persistence.*;

@Entity(name = "reservas")
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String apellido;
    private String telefono;
    private Date fechaIngreso;
    private String HoraIngreso;
    private Date fechaEgreso;
    private String HoraEgreso;

    @ManyToMany
    @JoinTable(name = "habitaciones_reservadas", 
    joinColumns = @JoinColumn(name = "reserva_id"), 
    inverseJoinColumns = {
        @JoinColumn(name = "habitacion_numero", referencedColumnName = "numero"),
        @JoinColumn(name = "habitacion_tipo", referencedColumnName = "tipo")
    })
    private List<Habitacion> habitacionesReservadas;

    @OneToOne(mappedBy = "reserva")
    private Estadia estadia;

    //constructores

    public Reserva(){}

    public Reserva(String nombre, String apellido, String telefono, Date fechaIngreso, String horaIngreso, Date fechaEgreso, String horaEgreso, List<Habitacion> habitacionesReservadas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        HoraIngreso = horaIngreso;
        this.fechaEgreso = fechaEgreso;
        HoraEgreso = horaEgreso;
        this.habitacionesReservadas = habitacionesReservadas;
    }
    
    //getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public Date getFechaIngreso() { return fechaIngreso; }
    public String getHoraIngreso() { return HoraIngreso; }
    public Date getFechaEgreso() { return fechaEgreso; }
    public String getHoraEgreso() { return HoraEgreso; }
    public List<Habitacion> getHabitacionesReservadas() { return habitacionesReservadas; }
    public Estadia getEstadia() { return estadia; }

    //setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public void setHoraIngreso(String horaIngreso) { HoraIngreso = horaIngreso; }
    public void setFechaEgreso(Date fechaEgreso) { this.fechaEgreso = fechaEgreso; }
    public void setHoraEgreso(String horaEgreso) { HoraEgreso = horaEgreso; }
    public void setHabitacionesReservadas(List<Habitacion> habitacionesReservadas) { this.habitacionesReservadas = habitacionesReservadas; }
    public void setEstadia(Estadia estadia) { this.estadia = estadia; }
}