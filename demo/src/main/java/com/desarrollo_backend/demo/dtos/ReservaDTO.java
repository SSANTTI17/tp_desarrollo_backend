package com.desarrollo_backend.demo.dtos;

import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class ReservaDTO {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;

    // CAMBIO: Ahora son String para recibir lo que manda el Front tal cual
    private String fechaIngreso;
    private String horaIngreso;
    private String fechaEgreso;
    private String horaEgreso;

    private List<HabitacionDTO> habitacionesReservadas;

    public ReservaDTO() {
    }

    public ReservaDTO(Reserva reserva) {
        this.id = reserva.getId();
        this.nombre = reserva.getNombre();
        this.apellido = reserva.getApellido();
        this.telefono = reserva.getTelefono();

        // Convertimos Date -> String para devolver al front
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.fechaIngreso = reserva.getFechaIngreso() != null ? sdf.format(reserva.getFechaIngreso()) : null;
        this.fechaEgreso = reserva.getFechaEgreso() != null ? sdf.format(reserva.getFechaEgreso()) : null;

        this.horaIngreso = reserva.getHoraIngreso();
        this.horaEgreso = reserva.getHoraEgreso();
        this.habitacionesReservadas = convertirADTO(reserva.getHabitacionesReservadas());
    }

    // Getters y Setters

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

    // Getters/Setters ahora de tipo String
    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(String fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getHoraEgreso() {
        return horaEgreso;
    }

    public void setHoraEgreso(String horaEgreso) {
        this.horaEgreso = horaEgreso;
    }

    public List<HabitacionDTO> getHabitacionesReservadas() {
        return habitacionesReservadas;
    }

    public void setHabitacionesReservadas(List<HabitacionDTO> habitacionesReservadas) {
        this.habitacionesReservadas = habitacionesReservadas;
    }

    // Auxiliar para pasar a entidades
    public List<Habitacion> getHabitacionesReservadasEntidad() {
        return convertirFromDTO(habitacionesReservadas);
    }

    public List<HabitacionDTO> convertirADTO(List<Habitacion> listaOriginal) {
        if (listaOriginal == null)
            return new ArrayList<>();
        List<HabitacionDTO> salida = new ArrayList<>();
        for (Habitacion h : listaOriginal) {
            salida.add(new HabitacionDTO(h));
        }
        return salida;
    }

    public List<Habitacion> convertirFromDTO(List<HabitacionDTO> listaDTO) {
        if (listaDTO == null)
            return new ArrayList<>();
        List<Habitacion> salida = new ArrayList<>();
        for (HabitacionDTO hDTO : listaDTO) {
            salida.add(new Habitacion(hDTO));
        }
        return salida;
    }
}