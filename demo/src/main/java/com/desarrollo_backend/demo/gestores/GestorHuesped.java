package com.desarrollo_backend.demo.gestores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.repository.HuespedRepository;


@Service
public class GestorHuesped{
    
    @Autowired
    private HuespedRepository huespedRepository;

    
    public Huesped darDeAltaHuesped(HuespedDTO huespedDto) {
        if (huespedDto == null) return null;

        Huesped huespedGuardar = new Huesped(huespedDto);
        return huespedRepository.save(huespedGuardar);
    }

    public List<HuespedDTO> buscarHuespedes(HuespedDTO filtro) {
        Specification<Huesped> spec = Specification.unrestricted(); // base vacía
        String apellido = filtro.getApellido();
        String nombre = filtro.getNombre();
        String dni = filtro.getNroDocumento();
        //String tipoDocumento = filtro.getTipo_documento().toString();

        if (apellido != null && !apellido.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.lower(root.get("apellido")), apellido.toLowerCase())
            );
        }

        if (nombre != null && !nombre.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.lower(root.get("nombre")), nombre.toLowerCase())
            );
        }

        if (dni != null && !dni.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("id").get("nroDocumento"), dni)
            );
        }

        if (filtro.getTipo_documento() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("id").get("tipo_documento"), filtro.getTipo_documento())
            );
        }

        List<Huesped> entidades = huespedRepository.findAll(spec);

        
        return entidades.stream()
            .map(this::convertirADTO) // Llamamos a un método auxiliar por cada huesped
            .collect(Collectors.toList());
    }

    private HuespedDTO convertirADTO(Huesped entidad) {
        HuespedDTO dto = new HuespedDTO(entidad);
        return dto;
    }

    public boolean huespedIsAlojado(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        Huesped huesped = huespedRepository.findById(id).get(); 
        return huesped.isAlojado();
    }
    
    //creo que le falta agregar para que elimine al responsable de pago (en que parte se asocia un responsable de pago al huesped?)
    // porque si el huesped nunca se alojó (solo reserva) no debería tener un responsable de pago
    public void eliminarHuesped(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        huespedRepository.deleteById(id);
    }


}

