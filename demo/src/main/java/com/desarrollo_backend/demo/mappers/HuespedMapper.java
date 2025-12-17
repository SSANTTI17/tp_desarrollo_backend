package com.desarrollo_backend.demo.mappers;

import org.springframework.stereotype.Component;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;

@Component
public class HuespedMapper {

    /**
     * Convierte una entidad Huesped a HuespedDTO.
     */
    public HuespedDTO toDto(Huesped entity) {
        if (entity == null) {
            return null;
        }

        HuespedDTO dto = new HuespedDTO();

        // Mapeo de atributos simples
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setNacionalidad(entity.getNacionalidad());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setOcupacion(entity.getOcupacion());
        dto.setAlojado(entity.isAlojado());
        dto.setDireccion(entity.getDireccion());
        dto.setBorradoLogico(entity.getBorradoLogico());

        // Mapeo de fecha (Date a Date) - Usa el setter generado por Lombok
        dto.setFechaDeNacimiento(entity.getFechaDeNacimiento());

        // Mapeo de la clave compuesta (HuespedPK -> DTO fields)
        if (entity.getId() != null) {
            dto.setTipo_documento(entity.getId().getTipo_documento());
            dto.setNroDocumento(entity.getId().getNroDocumento());
        }

        // Nota: CUIT y PosiciónIVA no están en la entidad Huesped (están en
        // PersonaFisica),
        // por lo que quedan nulos en este mapeo simple.

        return dto;
    }

    /**
     * Convierte un HuespedDTO a una entidad Huesped.
     */
    public Huesped toEntity(HuespedDTO dto) {
        if (dto == null) {
            return null;
        }

        Huesped entity = new Huesped();

        // Mapeo de atributos simples
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setNacionalidad(dto.getNacionalidad());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setOcupacion(dto.getOcupacion());
        entity.setAlojado(dto.isAlojado());

        // Manejo de fecha (Date a Date)
        entity.setFechaDeNacimiento(dto.getFechaDeNacimiento());

        // Manejo de nulos para dirección y borrado
        entity.setDireccion(dto.getDireccion() != null ? dto.getDireccion() : "Sin dirección");
        entity.setBorradoLogico(dto.getBorradoLogico() != null ? dto.getBorradoLogico() : false);

        // Mapeo de la clave compuesta (DTO fields -> HuespedPK)
        if (dto.getTipo_documento() != null && dto.getNroDocumento() != null) {
            entity.setId(new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento()));
        }

        return entity;
    }

    /**
     * Actualiza una entidad existente con los datos de un DTO (sin tocar la PK).
     */
    public void updateEntity(Huesped entity, HuespedDTO dto) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setFechaDeNacimiento(dto.getFechaDeNacimiento());
        entity.setNacionalidad(dto.getNacionalidad());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setOcupacion(dto.getOcupacion());

        if (dto.getDireccion() != null) {
            entity.setDireccion(dto.getDireccion());
        }

        if (dto.getBorradoLogico() != null) {
            entity.setBorradoLogico(dto.getBorradoLogico());
        }
    }
}