package com.desarrollo_backend.demo.gestores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.mappers.HuespedMapper; // Importamos el Mapper
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.repository.HuespedRepository;

@Service
public class GestorHuesped {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HuespedMapper huespedMapper; // Inyectamos el Mapper

    public Huesped darDeAltaHuesped(HuespedDTO huespedDto) {
        if (huespedDto == null)
            return null;

        // CORRECCIÓN: Usamos el mapper en lugar del constructor
        Huesped huespedGuardar = huespedMapper.toEntity(huespedDto);
        return huespedRepository.save(huespedGuardar);
    }

    public List<Huesped> buscarHuespedes(HuespedDTO filtro) {
        Specification<Huesped> spec = Specification.unrestricted();
        String apellido = filtro.getApellido();
        String nombre = filtro.getNombre();
        String dni = filtro.getNroDocumento();

        if (apellido != null && !apellido.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("apellido")), apellido.toLowerCase() + "%"));
        }

        if (nombre != null && !nombre.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nombre")), nombre.toLowerCase() + "%"));
        }

        if (dni != null && !dni.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("id").get("nroDocumento"), dni + "%"));
        }

        if (filtro.getTipo_documento() != null) {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(root.get("id").get("tipo_documento"), filtro.getTipo_documento()));
        }

        return huespedRepository.findAll(spec);
    }

    public boolean huespedIsAlojado(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        // Es mejor usar ifPresent o orElse(null) para evitar NoSuchElementException
        return huespedRepository.findById(id)
                .map(Huesped::isAlojado)
                .orElse(false);
    }

    public void eliminarHuesped(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        huespedRepository.deleteById(id);
    }

    @Transactional
    public void modificarHuesped(HuespedDTO dto, HuespedPK pkAnterior, boolean modificoPK) {

        if (modificoPK) {
            // CASO 1: Cambio de Identidad (PK)

            // A. Dar de baja lógica al anterior
            Huesped huespedAnterior = huespedRepository.findById(pkAnterior).orElse(null);
            if (huespedAnterior != null) {
                huespedAnterior.setBorradoLogico(true);
                huespedRepository.save(huespedAnterior);
            }

            // B. Crear el nuevo huésped usando el Mapper
            Huesped huespedNuevo = huespedMapper.toEntity(dto);
            huespedNuevo.setBorradoLogico(false);
            huespedRepository.save(huespedNuevo);

        } else {
            // CASO 2: Modificación de datos simples
            HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
            Huesped existente = huespedRepository.findById(id).orElse(null);

            if (existente != null) {
                // CORRECCIÓN: Usamos updateEntity del Mapper en lugar de setHuesped(dto)
                huespedMapper.updateEntity(existente, dto);

                // Aseguramos consistencia
                existente.setBorradoLogico(false);
                huespedRepository.save(existente);
            }
        }
    }

    public Huesped obtenerHuespedPorId(HuespedPK id) {
        return huespedRepository.findById(id).orElse(null);
    }
}