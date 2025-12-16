package com.desarrollo_backend.demo.controladores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.ContenedorEstadiaYFacturaDTO;
import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.FacturaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.facade.FachadaHotel;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.dtos.requests.*;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorFacturacion {

    @Autowired
    private FachadaHotel fachada;


    // CU07 - Paso 1: Buscar ocupantes
    @GetMapping("/ocupantes")
    public ResponseEntity<?> obtenerHuespedesParaFacturacion(
            @RequestParam String habitacion, // Ejemplo: "IE101", "SFP606"
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaSalida) {
        try {
            // Lógica de presentación: Separar el string de habitación
            if (habitacion == null || habitacion.length() < 4) {
                return ResponseEntity.badRequest().body("Formato de habitación inválido. Debe ser Tipo+Numero (ej: IE101)");
            }

            // Los últimos 3 caracteres son el número, el resto es el tipo
            String tipoStr = habitacion.substring(0, habitacion.length() - 3);
            String numeroStr = habitacion.substring(habitacion.length() - 3);

            // Crear y configurar HabitacionDTO
            HabitacionDTO habitacionDTO = new HabitacionDTO();
            habitacionDTO.setNumero(Integer.parseInt(numeroStr));
            habitacionDTO.setTipo(TipoHabitacion.fromString(tipoStr));

            // Crear y configurar EstadiaDTO con la fecha que llega del front
            EstadiaDTO estadiaDTO = new EstadiaDTO();
            estadiaDTO.setFechaFin(fechaSalida);

            // Llamada a la fachada
            List<HuespedDTO> huespedes = fachada.obtenerHuespedesParaFacturacion(estadiaDTO, habitacionDTO);

            return ResponseEntity.ok(huespedes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error buscando ocupantes: " + e.getMessage());
        }
    }

    // CU07 - Paso 2: Obtener ítems pendientes
    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestBody GenerarFacturaRequest request) {
        try {
            // Llamada a la fachada pasando los DTOs recibidos
            ContenedorEstadiaYFacturaDTO contenedor = fachada.generarFactura(
                    request.getHuesped(),
                    request.getCuit(),
                    request.getEstadia(),
                    request.getHabitacion()
            );
            return ResponseEntity.ok(contenedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar factura: " + e.getMessage());
        }
    }


    // CU07 - Paso final: Confirmar Factura
    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarFactura(@RequestBody ConfirmarFacturaRequest request) {
        try {
            // Llamada a la fachada
            FacturaDTO facturaConfirmada = fachada.confirmarFactura(
                    request.getIdEstadia(),
                    request.getFactura(),
                    request.getHuesped(),
                    request.getResponsable(),
                    request.getConsumos()
            );
            return ResponseEntity.ok(facturaConfirmada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al confirmar factura: " + e.getMessage());
        }
    }
}