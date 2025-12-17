package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.gestores.GestorContable;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.mappers.HuespedMapper; // Importamos el Mapper
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

    @Autowired
    private HuespedMapper huespedMapper; // Inyectamos el Mapper

    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedesAPI(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento) {

        // Creamos el filtro manualmente (el DTO vacío sí funciona por el
        // @NoArgsConstructor)
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

        List<Huesped> resultados = gestorHuesped.buscarHuespedes(filtro);

        // CORRECCIÓN: Transformación usando el mapper
        List<HuespedDTO> dtos = resultados.stream()
                .map(huespedMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearHuespedAPI(@RequestBody ContenedorDeAltaHuesped request) {
        try {
            // El gestor ya usa el mapper internamente para el alta
            Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(request.getHuesped());

            if (request.getPersonaFisica() != null &&
                    request.getPersonaFisica().getCUIT() != null &&
                    !request.getPersonaFisica().getCUIT().isEmpty()) {

                gestorContable.registrarPersonaFisica(
                        request.getPersonaFisica(), huespedGuardado);
            }

            return ResponseEntity.ok().body(
                    Map.of("message", "Huesped y Responsable creados exitosamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarHuesped(
            @RequestBody HuespedDTO modificado,
            @RequestParam boolean modificoPK,
            @RequestParam(required = false) String oldTipo,
            @RequestParam(required = false) String oldDni) {

        if (modificoPK && (oldTipo == null || oldDni == null)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Si se modifica la clave, debe enviar oldTipo y oldDni"));
        }

        if (modificoPK) {
            HuespedPK idNuevo = new HuespedPK(modificado.getTipo_documento(), modificado.getNroDocumento());
            Huesped huespedExistente = gestorHuesped.obtenerHuespedPorId(idNuevo);

            if (huespedExistente != null && !Boolean.TRUE.equals(huespedExistente.getBorradoLogico())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error",
                                "El nuevo tipo y número de documento ya están ocupados por otro huésped activo."));
            }
        }

        try {
            HuespedPK pkAnterior = null;
            if (modificoPK) {
                try {
                    TipoDoc tipoAnteriorEnum = TipoDoc.valueOf(oldTipo);
                    pkAnterior = new HuespedPK(tipoAnteriorEnum, oldDni);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Tipo de documento anterior inválido"));
                }
            } else {
                pkAnterior = new HuespedPK(modificado.getTipo_documento(), modificado.getNroDocumento());
            }

            gestorHuesped.modificarHuesped(modificado, pkAnterior, modificoPK);
            gestorContable.modificarHuesped(modificado);

            return ResponseEntity.ok(Map.of("message", "La operación ha culminado con éxito"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al procesar la modificación: " + e.getMessage()));
        }
    }

    @PostMapping("/verificar-baja")
    public ResponseEntity<?> verificarBaja(@RequestBody HuespedDTO huespedDTO) {
        boolean alojado = gestorHuesped.huespedIsAlojado(huespedDTO);

        if (alojado) {
            return ResponseEntity.ok(Map.of(
                    "puedeEliminar", false,
                    "mensaje",
                    "El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad. PRESIONE CUALQUIER TECLA PARA CONTINUAR…"));
        } else {
            String mensaje = String.format(
                    "Los datos del huésped %s %s, %s y %s serán eliminados del sistema",
                    huespedDTO.getNombre(),
                    huespedDTO.getApellido(),
                    huespedDTO.getTipo_documento(),
                    huespedDTO.getNroDocumento());

            return ResponseEntity.ok(Map.of(
                    "puedeEliminar", true,
                    "mensaje", mensaje));
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestBody HuespedDTO huespedDTO) {
        gestorHuesped.eliminarHuesped(huespedDTO);

        String mensaje = String.format(
                "Los datos del huésped %s %s, %s y %s han sido eliminados del sistema. PRESIONE CUALQUIER TECLA PARA CONTINUAR…",
                huespedDTO.getNombre(),
                huespedDTO.getApellido(),
                huespedDTO.getTipo_documento(),
                huespedDTO.getNroDocumento());

        return ResponseEntity.ok(Map.of("mensaje", mensaje));
    }
}