package com.desarrollo_backend.demo.controladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.facade.FachadaHotel;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;

@RestController
@RequestMapping("/api/huespedes")
@CrossOrigin(origins = "http://localhost:3000")
public class ControladorHuesped {

    @Autowired
    private FachadaHotel fachadaHotel;

    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedesAPI(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String documento) {

        // 1. Construimos el DTO filtro con los datos que vienen del Front
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
                // Podrías retornar un Bad Request aquí si prefieres ser estricto
            }
        }

        if (documento != null && !documento.isEmpty())
            filtro.setNroDocumento(documento);

        // 2. Llamamos a la Fachada
        // La fachada ya se encarga de llamar al gestor y mappear los resultados a DTO
        List<HuespedDTO> dtos = fachadaHotel.buscarHuespedes(filtro);

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearHuespedAPI(@RequestBody ContenedorDeAltaHuesped request) {
        try {
            // 1. Extraemos el DTO del huesped que viene en el contenedor
            // Ignoramos la parte de PersonaFisica/Juridica como pediste
            HuespedDTO huespedDTO = request.getHuesped();

            // 2. Usamos el método simple que YA TENÍAS en la fachada
            HuespedDTO creado = fachadaHotel.registrarHuesped(huespedDTO);

            // 3. Retornamos el huésped creado (o un mensaje si prefieres)
            return ResponseEntity.ok().body(Map.of(
                    "message", "Huésped creado exitosamente",
                    "huesped", creado));

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

        try {
            // Delegamos toda la lógica compleja a la Fachada
            fachadaHotel.modificarHuesped(modificado, modificoPK, oldTipo, oldDni);

            return ResponseEntity.ok(Map.of("message", "La operación ha culminado con éxito"));

        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en consola del servidor
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verificar-baja")
    public ResponseEntity<?> verificarBaja(@RequestBody HuespedDTO huespedDTO) {
        try {
            // Delegamos todo a la Fachada
            Map<String, Object> respuesta = fachadaHotel.verificarBaja(huespedDTO);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestBody HuespedDTO huespedDTO) {
        try {
            // Llamamos a la fachada y obtenemos el mensaje listo
            String mensaje = fachadaHotel.eliminarHuesped(huespedDTO);

            return ResponseEntity.ok(Map.of("mensaje", mensaje));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}