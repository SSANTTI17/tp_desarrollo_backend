package com.desarrollo_backend.demo.modelo.estadias;

import java.util.Date;
import com.desarrollo_backend.demo.modelo.factura.*;
import com.desarrollo_backend.demo.modelo.habitacion.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity
@Table(name = "estadias")
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "habitacion_numero", referencedColumnName = "numero"),
            @JoinColumn(name = "habitacion_tipo", referencedColumnName = "tipo")
    })
    private Habitacion habitacion;

    @Column(nullable = false)
    private float precio;

    @Column(nullable = false)
    private Date fechaInicio;

    @Column(nullable = false)
    private Date fechaFin;

    @OneToMany(mappedBy = "estadia")
    private List<Factura> facturas;

    @Column(nullable = false)
    private boolean facturadaEstadia = false;

    @OneToOne(optional = true) // puede no tneer una reserva asociada
    @JoinColumn(name = "reserva_id", nullable = true)
    private Reserva reserva;

    @OneToMany(mappedBy = "estadia")
    private List<Consumo> consumos;

    @ManyToMany
    private List<Huesped> huespedes;

    public void agregarConsumo(Consumo c) {
        consumos.add(c);
    }

    public void agregarHuesped(Huesped h) {
        huespedes.add(h);
    }

    public void agregarFactura(Factura f) {
        facturas.add(f);
    }

    public float totalConsumos() {
        float total = 0;
        for (Consumo c : consumos) {
            if (c.isFacturado() == false)
                total += c.getMonto();
        }
        return total;
    }

    // consutructores
    public Estadia() {
        consumos = new ArrayList<Consumo>();
        huespedes = new ArrayList<Huesped>();
        facturas = new ArrayList<Factura>();
    }

    public Estadia(Reserva reserva, Date fechaInicio) {
        this.reserva = reserva;
        this.fechaInicio = fechaInicio;
        consumos = new ArrayList<Consumo>();
        huespedes = new ArrayList<Huesped>();
        facturas = new ArrayList<Factura>();
    }

    public Estadia(Reserva reserva, Date fechaInicio, Date fechaFin) {
        this.reserva = reserva;
        this.fechaInicio = fechaInicio;
        consumos = new ArrayList<Consumo>();
        huespedes = new ArrayList<Huesped>();
        facturas = new ArrayList<Factura>();
    }

    // getters
    public int getId() {
        return id;
    }

    public TipoHabitacion geTipoHabitacion() {
        return habitacion.getTipo();
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

    public List<Factura> getFactura() {
        return facturas;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public List<Consumo> getConsumos() {
        return consumos;
    }

    public List<Huesped> getHuespedes() {
        return huespedes;
    }

    public boolean isHabitacionFacturada() {
        return facturadaEstadia;
    }

    // setters

    public void setFacturadaEstadia(boolean facturadaEstadia) {
        this.facturadaEstadia = facturadaEstadia;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConsumos(List<Consumo> consumos) {
        this.consumos = consumos;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setHabitacion(Habitacion h) {
        this.habitacion = h;
    }

    public void setTipoHabitacion(TipoHabitacion t) {
        habitacion.setTipo(t);
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setFactura(List<Factura> factura) {
        this.facturas = factura;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setHuespedes(List<Huesped> huespedes) {
        this.huespedes = huespedes;
    }
}
