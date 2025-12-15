package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desarrollo_backend.demo.gestores.GestorContable;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorHuesped {

    @Autowired
    private GestorHuesped gestorHuesped;

    @Autowired
    private GestorContable gestorContable;

    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedesAPI(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento) {

        HuespedDTO filtro = new HuespedDTO();

        if (nombre != null && !nombre.isEmpty())
            filtro.setNombre(nombre);

        if (apellido != null && !apellido.isEmpty())
            filtro.setApellido(apellido);

        if (tipoDocumento != null && !tipoDocumento.trim().isEmpty()) {
            try {
                filtro.setTipo_documento(TipoDoc.valueOf(tipoDocumento));
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo de documento inválido: " + tipoDocumento);
            }
        }

        if (documento != null && !documento.isEmpty())
            filtro.setNroDocumento(documento);

        List<HuespedDTO> resultados = gestorHuesped.buscarHuespedes(filtro);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearHuespedAPI(@RequestBody ContenedorDeAltaHuesped request) {
        try {
            Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(request.getHuesped());

            if (request.getPersonaFisica() != null &&
                request.getPersonaFisica().getCUIT() != null &&
                !request.getPersonaFisica().getCUIT().isEmpty()) {

                gestorContable.registrarPersonaFisica(
                        request.getPersonaFisica(), huespedGuardado);
            }

            return ResponseEntity.ok().body(
                Map.of("message", "Huesped y Responsable creados exitosamente")
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(@RequestBody HuespedDTO modificado, @RequestParam boolean modificoPK) {

        // 1. Verificamos si la "nueva" PK ya existe en la base de datos
        HuespedPK idNuevo = new HuespedPK(modificado.getTipo_documento(), modificado.getNroDocumento());
        Huesped huespedExistente = gestorHuesped.obtenerHuespedPorId(idNuevo);

        // 2. Validación: Si cambió la PK y ya existe alguien con ese documento -> ERROR
        if (modificoPK && huespedExistente != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El tipo y número de documento ya existen en el sistema"));
        }

        // 3. Si pasa la validación, procedemos a guardar
        try {
            // Actualizamos datos contables 
            gestorContable.modificarHuesped(modificado);
            
            // Actualizamos datos del huésped y dirección
            gestorHuesped.modificarHuesped(modificado);

            return ResponseEntity.ok(
                Map.of("message", "La operación ha culminado con éxito")
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al procesar la modificación: " + e.getMessage()));
        }
    }


    // verificar si el huesped puede ser eliminado
    @PostMapping("/verificar-baja")
    public ResponseEntity<?> verificarBaja(@RequestBody HuespedDTO huespedDTO) {
        boolean alojado = gestorHuesped.huespedIsAlojado(huespedDTO);

        if (alojado) {
            // Retorna mensaje de error si se alojó alguna vez
            return ResponseEntity.ok(Map.of(
                "puedeEliminar", false,
                "mensaje", "El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad. PRESIONE CUALQUIER TECLA PARA CONTINUAR…"
            ));
        } else {
            // Retorna mensaje de confirmación
            String mensaje = String.format(
                "Los datos del huésped %s %s, %s y %s serán eliminados del sistema",
                huespedDTO.getNombre(),
                huespedDTO.getApellido(),
                huespedDTO.getTipo_documento(),
                huespedDTO.getNroDocumento()
            );

            return ResponseEntity.ok(Map.of(
                "puedeEliminar", true,
                "mensaje", mensaje
            ));
        }
    }

    // eliminar el huésped
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestBody HuespedDTO huespedDTO) {

        gestorHuesped.eliminarHuesped(huespedDTO);

        String mensaje = String.format(
            "Los datos del huésped %s %s, %s y %s han sido eliminados del sistema. PRESIONE CUALQUIER TECLA PARA CONTINUAR…",
            huespedDTO.getNombre(),
            huespedDTO.getApellido(),
            huespedDTO.getTipo_documento(),
            huespedDTO.getNroDocumento()
        );

        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }
    
}
