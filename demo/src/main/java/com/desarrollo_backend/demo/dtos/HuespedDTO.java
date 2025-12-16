package com.desarrollo_backend.demo.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.desarrollo_backend.demo.modelo.huesped.*;

public class HuespedDTO {
    // mismos atributos que Huesped
    private String nombre;
    private String apellido;
    private TipoDoc tipo_documento;
    private String nroDocumento;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaDeNacimiento;
    private String nacionalidad;
    private String email;
    private String telefono;
    private String ocupacion;
    private boolean alojado;
    private String direccion;
    private String CUIT;
    private String posicionIVA;
    private Boolean borradoLogico;

    // Constructor desde Huesped
    public HuespedDTO(Huesped huesped) {
        this.nombre = huesped.getNombre();
        this.apellido = huesped.getApellido();
        this.tipo_documento = huesped.getTipo_documento();
        this.nroDocumento = huesped.getNroDocumento();
        this.fechaDeNacimiento = huesped.getFechaDeNacimiento();
        this.nacionalidad = huesped.getNacionalidad();
        this.email = huesped.getEmail();
        this.telefono = huesped.getTelefono();
        this.ocupacion = huesped.getOcupacion();
        this.alojado = huesped.isAlojado();
        // CORRECCIÓN: Obtener la dirección real
        this.direccion = huesped.getDireccion();
        this.borradoLogico = huesped.getBorrado();
    }

    public HuespedDTO() {
        this.nombre = "";
        this.apellido = "";
        this.tipo_documento = null;
        this.nroDocumento = "";
        this.fechaDeNacimiento = null;
        this.nacionalidad = "";
        this.email = "";
        this.telefono = "";
        this.ocupacion = "";
        this.alojado = false;
        this.direccion = null;
        this.borradoLogico=false;
    }
    // Getters

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public TipoDoc getTipo_documento() {
        return tipo_documento;
    }

    public String getNroDocumento() {
        return nroDocumento;
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

    public String getCUIT() {
        return CUIT;
    }

    public String getPosicionIVA() {
        return posicionIVA;
    }

    public Boolean getBorrado(){
        return borradoLogico;
    }

    // Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTipo_documento(TipoDoc tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // hacemos una sobrecarga de setFechaDeNacimiento para aceptar String
    public void setFechaDeNacimiento(String fechaString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (fechaString != null && !fechaString.isEmpty()) {
                this.fechaDeNacimiento = sdf.parse(fechaString);
            } else {
                this.fechaDeNacimiento = null;
            }

        } catch (ParseException e) {
            // Si falla (ej: formato incorrecto), imprime un error
            System.out.println("Error al parsear la fecha: " + fechaString + " - " + e.getMessage());
            this.fechaDeNacimiento = null; // Asigna null si falla
        }
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public void setAlojado(boolean alojado) {
        this.alojado = alojado;
    }
    
}
