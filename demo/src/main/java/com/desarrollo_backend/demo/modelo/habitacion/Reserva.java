package com.desarrollo_backend.demo.modelo.habitacion;

import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.ReservaDTO;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;

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


    public Reserva() {
    }
    public Reserva(String nombre, String apellido, String telefono, Date fechaIngreso, String horaIngreso,
            Date fechaEgreso, String horaEgreso, List<Habitacion> habitacionesReservadas) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        this.horaIngreso = horaIngreso;
        this.fechaEgreso = fechaEgreso;
        this.horaEgreso = horaEgreso;
        this.habitacionesReservadas = habitacionesReservadas;
    }
    public Reserva(ReservaDTO reservaDTO) {
        this.nombre = reservaDTO.getNombre();
        this.apellido = reservaDTO.getApellido();
        this.telefono = reservaDTO.getTelefono();
        this.fechaIngreso = reservaDTO.getFechaIngreso();
        this.horaIngreso = reservaDTO.getHoraIngreso();
        this.fechaEgreso = reservaDTO.getFechaEgreso();
        this.horaEgreso = reservaDTO.getHoraEgreso();
        this.habitacionesReservadas = reservaDTO.getHabitacionesReservadas();
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

    public List<Habitacion> convertirFromDTO(List<HabitacionDTO> listaDTO){ 
        List<Habitacion> salida = new ArrayList<>();
        for(HabitacionDTO hDTO : listaDTO){
            salida.add(new Habitacion(hDTO));
        }
        return salida;
    }

}