package com.desarrollo_backend.demo.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuespedDTO {

    private String nombre;
    private String apellido;
    private TipoDoc tipo_documento;
    private String nroDocumento;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaDeNacimiento; // Lombok generará setFechaDeNacimiento(Date)

    private String nacionalidad;
    private String email;
    private String telefono;
    private String ocupacion;
    private boolean alojado;
    private String direccion;

    // Datos fiscales opcionales
    private String CUIT;
    private String posicionIVA;

    private Boolean borradoLogico;

    public void setFechaDeNacimientoDesdeString(String fechaString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (fechaString != null && !fechaString.isEmpty()) {
                this.fechaDeNacimiento = sdf.parse(fechaString);
            } else {
                this.fechaDeNacimiento = null;
            }
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha manual: " + fechaString);
            this.fechaDeNacimiento = null;
        }
    }
}