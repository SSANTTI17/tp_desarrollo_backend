package com.desarrollo_backend.demo.dtos.requests;

import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;

public class GenerarFacturaRequest {
        private HuespedDTO huesped;
        private String cuit;
        private EstadiaDTO estadia;
        private HabitacionDTO habitacion;

        // Getters y Setters
        public HuespedDTO getHuesped() { return huesped; }
        public void setHuesped(HuespedDTO huesped) { this.huesped = huesped; }
        public String getCuit() { return cuit; }
        public void setCuit(String cuit) { this.cuit = cuit; }
        public EstadiaDTO getEstadia() { return estadia; }
        public void setEstadia(EstadiaDTO estadia) { this.estadia = estadia; }
        public HabitacionDTO getHabitacion() { return habitacion; }
        public void setHabitacion(HabitacionDTO habitacion) { this.habitacion = habitacion; }

}
