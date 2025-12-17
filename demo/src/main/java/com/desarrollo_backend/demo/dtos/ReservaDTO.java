package com.desarrollo_backend.demo.dtos;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import java.util.List;
import java.util.Date;

public class ReservaDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private Date fechaIngreso;
    private String horaIngreso;
    private Date fechaEgreso;
    private String horaEgreso;
    private List<Habitacion> habitacionesReservadas;

    public ReservaDTO(String nombre, String apellido, String telefono, Date fechaIngreso, String horaIngreso,
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
    public ReservaDTO(Reserva reserva) {
        this.nombre = reserva.getNombre();
        this.apellido = reserva.getApellido();
        this.telefono = reserva.getTelefono();
        this.fechaIngreso = reserva.getFechaIngreso();
        this.horaIngreso = reserva.getHoraIngreso();
        this.fechaEgreso = reserva.getFechaEgreso();
        this.horaEgreso = reserva.getHoraEgreso();
        this.habitacionesReservadas = reserva.getHabitacionesReservadas();
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
}