package com.desarrollo_backend.demo.modelo.cheque;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class ListadoCheques {
    
    private Date fechaInicio;
    private Date fechaFin;
    private List<Cheque> cheques;

    public ListadoCheques(Date fechaInicio, Date fechaFin, List<Cheque> cheques){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cheques = cheques;
    }

    public ListadoCheques(Date fechaInicio, Date fechaFin){
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cheques = new ArrayList<>();
    }

    //getter
    public Date getFechaInicio() { return fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public List<Cheque> getCheques() { return cheques; }
    //setter
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public void setCheques(List<Cheque> cheques) { this.cheques = cheques; }

}
