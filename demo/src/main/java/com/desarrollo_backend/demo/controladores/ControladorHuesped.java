package com.desarrollo_backend.demo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.desarrollo_backend.demo.gestores.GestorContable;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;

@Controller
public class ControladorHuesped {

    @Autowired
    private GestorHuesped gestorHuesped;

    @Autowired
    private GestorContable gestorContable;

    @GetMapping("/altaHuesped")
    public String altaHuesped(Model model) {
        model.addAttribute("huesped", new HuespedDTO());
        model.addAttribute("tiposDocumento", TipoDoc.values());
        return "altaHuesped";
    }

    @PostMapping("/altaHuesped")
    public String crearHuesped(@ModelAttribute HuespedDTO huespedDTO) {
        gestorHuesped.darDeAltaHuesped(huespedDTO);
        return "redirect:/";
    }

    @GetMapping("/buscarHuesped")
    public String buscarHuesped(Model model) {
        model.addAttribute("tiposDocumento", TipoDoc.values());
        return "buscarHuesped";
    }

    @GetMapping("/api/huespedes/buscar")
    @ResponseBody
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

    @GetMapping("/estado")
    public String mostrarPaginaEstado() {
        return "estadoHabitacion";
    }

    @GetMapping("/reservar")
    public String mostrarPaginaReserva() {
        return "reservarHabitacion";
    }

    @PostMapping("/api/huespedes/crear")
    @ResponseBody
    public ResponseEntity<?> crearHuespedAPI(@RequestBody ContenedorDeAltaHuesped request) {
        try {
            Huesped huespedGuardado = gestorHuesped.darDeAltaHuesped(request.getHuesped());
            if (request.getPersonaFisica() != null &&
                    request.getPersonaFisica().getCUIT() != null &&
                    !request.getPersonaFisica().getCUIT().isEmpty()) {

                gestorContable.registrarPersonaFisica(request.getPersonaFisica(), huespedGuardado);
            }

            return ResponseEntity.ok().body("{\"message\": \"Huesped y Responsable creados exitosamente\"}");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
