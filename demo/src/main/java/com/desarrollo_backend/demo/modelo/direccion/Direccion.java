package com.desarrollo_backend.demo.modelo.direccion;

import jakarta.persistence.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private int codigoPostal;
    
    @Column(nullable = false, length = 100)
    private String calle;

    @Column(nullable = true, length = 100)
    private String departamento;

    @Column(nullable = false, length = 100)
    private int altura;

    @Column(nullable = true, length = 100)
    private int piso;

    @ManyToOne
    @JoinColumn(name = "localidad_nombre")
    private Localidad localidad;

    @OneToMany(mappedBy = "direccion")
    private List<Huesped> huespedes;

    public Direccion(){}

    public Direccion(String calle, String departamento, int altura, int piso, int codigoPostal, Localidad localidad) {
        this.calle = calle;
        this.altura = altura;
        this.piso = piso;
        this.departamento = departamento;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        huespedes = new ArrayList<>();
    }

    public void agregarHuesped(Huesped h){
        huespedes.add(h);
    }

    //Getters
    public String getCalle() { return calle; }
    public String getDepartamento() { return departamento; }
    public int getAltura() { return altura; }
    public int getPiso() { return piso; }
    public int getCodigoPostal() { return codigoPostal; }
    public Localidad getLocalidad() { return localidad; }

    //setters
    public void setCalle(String calle) { this.calle = calle; }  
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setAltura(int altura) { this.altura = altura; }
    public void setPiso(int piso) { this.piso = piso; }
    public void setCodigoPostal(int codigoPostal) { this.codigoPostal = codigoPostal; }
    public void setLocalidad(Localidad localidad){ this.localidad = localidad; }
    

}
