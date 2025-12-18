package com.desarrollo_backend.demo.dtos;

import java.util.Date;
import com.desarrollo_backend.demo.modelo.factura.*;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.desarrollo_backend.demo.modelo.estadias.Consumo;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import java.util.List;
import java.util.ArrayList;

public class EstadiaDTO {

    private HabitacionDTO habitacion;
    private float precio;
    private Date fechaInicio;
    private Date fechaFin;
    private Factura factura; // Ojo: Esto es una Entidad, idealmente debería ser FacturaDTO, pero lo dejamos
                             // para que compile.

    @JsonIgnore
    private ReservaDTO reserva;

    private List<ConsumoDTO> consumos;
    private int id;

    public void agregarConsumo(Consumo c) {
        if (this.consumos == null)
            this.consumos = new ArrayList<>();
        ConsumoDTO consumoDTO = new ConsumoDTO(c);
        consumos.add(consumoDTO);
    }

    // constructores
    public EstadiaDTO() {
    }

    public EstadiaDTO(Estadia estadia) {
        this.id = estadia.getId();
        this.habitacion = new HabitacionDTO(estadia.getHabitacion());
        this.precio = estadia.getPrecio();
        this.fechaInicio = estadia.getFechaInicio();
        this.fechaFin = estadia.getFechaFin();

        // --- CORRECCIÓN CRÍTICA AQUÍ ---
        // Verificamos si la estadía tiene reserva antes de convertirla
        if (estadia.getReserva() != null) {
            this.reserva = new ReservaDTO(estadia.getReserva());
        } else {
            this.reserva = null;
        }
        // -------------------------------

        this.consumos = this.transformarConsumos(estadia.getConsumos());
    }

    public EstadiaDTO(Reserva reserva, Date fechaInicio) {
        if (reserva != null) {
            this.reserva = new ReservaDTO(reserva);
        }
        this.fechaInicio = fechaInicio;
        consumos = new ArrayList<>();
    }

    public EstadiaDTO(Reserva reserva, Date fechaInicio, Date fechaFin) {
        if (reserva != null) {
            this.reserva = new ReservaDTO(reserva);
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        consumos = new ArrayList<>();
    }

    // getters
    public TipoHabitacion geTipoHabitacion() {
        return habitacion != null ? habitacion.getTipo() : null;
    }

    public int getId() {
        return id;
    }

    public float getPrecio() {
        return precio;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Factura getFactura() {
        return factura;
    }

    public ReservaDTO getReserva() {
        return reserva;
    }

    public List<ConsumoDTO> getConsumos() {
        return consumos;
    }

    // setters
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    private List<ConsumoDTO> transformarConsumos(List<Consumo> consumosEstadia) {
        if (consumosEstadia == null) {
            return new ArrayList<>();
        }
        List<ConsumoDTO> consumosTransformados = new ArrayList<>();
        for (Consumo consumo : consumosEstadia) {
            ConsumoDTO consumoDTO = new ConsumoDTO(consumo);
            consumosTransformados.add(consumoDTO);
        }
        return consumosTransformados;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoHabitacion getTipoHabitacion() {
        return habitacion != null ? habitacion.getTipo() : null;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setReserva(ReservaDTO reserva) {
        this.reserva = reserva;
    }
}