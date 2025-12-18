package com.desarrollo_backend.demo.controladores;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.ContenedorEstadiaYFacturaDTO;
import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.PersonaJuridicaDTO;
import com.desarrollo_backend.demo.facade.FachadaHotel;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.dtos.requests.*;
import com.desarrollo_backend.demo.exceptions.EdadInsuficienteException;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorFacturacion {

    @Autowired
    private FachadaHotel fachada;

    @GetMapping("/ocupantes")
    public ResponseEntity<?> obtenerHuespedesParaFacturacion(
            @RequestParam String habitacion,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaSalida) {
        try {
            if (habitacion == null || habitacion.length() < 4) {
                return ResponseEntity.badRequest().body("Formato inválido");
            }
            String tipoStr = habitacion.substring(0, habitacion.length() - 3);
            String numeroStr = habitacion.substring(habitacion.length() - 3);

            HabitacionDTO habitacionDTO = new HabitacionDTO();
            habitacionDTO.setNumero(Integer.parseInt(numeroStr));
            habitacionDTO.setTipo(TipoHabitacion.fromString(tipoStr));

            EstadiaDTO estadiaDTO = new EstadiaDTO();
            estadiaDTO.setFechaFin(fechaSalida);

            List<HuespedDTO> huespedes = fachada.obtenerHuespedesParaFacturacion(estadiaDTO, habitacionDTO);
            return ResponseEntity.ok(huespedes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody GenerarFacturaRequest request) {
        try {
            ContenedorEstadiaYFacturaDTO contenedor = fachada.generarFactura(
                    request.getHuesped(), request.getCuit(), request.getEstadia(), request.getHabitacion()
            );
            // Sin System.out.println para evitar NullPointer
            return ResponseEntity.ok(contenedor);
        } catch (EdadInsuficienteException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarFactura(@RequestBody ConfirmarFacturaRequest request) {
        try {
            // Pasamos también la lista de pagos
            FacturaDTO facturaConfirmada = fachada.confirmarFactura(
                    request.getIdEstadia(),
                    request.getFactura(),
                    request.getHuesped(),
                    request.getResponsable(),
                    request.getConsumos(),
                    request.getFormasPago()
            );
            return ResponseEntity.ok(facturaConfirmada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al confirmar: " + e.getMessage());
        }
    }

    @PostMapping("/responsable")
    public ResponseEntity<?> darDeAltaResponsable(@RequestBody PersonaJuridicaDTO request) {
        try {
            PersonaJuridicaDTO nuevoResponsable = fachada.darDeAltaResponsablePago(request);
            return ResponseEntity.ok(nuevoResponsable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error alta responsable: " + e.getMessage());
        }
    }
}