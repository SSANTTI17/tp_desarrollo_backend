package com.desarrollo_backend.demo.gestores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.mappers.HuespedMapper;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.repository.HuespedRepository;

@Service
public class GestorHuesped {

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HuespedMapper huespedMapper;

    public Huesped darDeAltaHuesped(HuespedDTO huespedDto) {
        if (huespedDto == null)
            return null;
        Huesped huespedGuardar = huespedMapper.toEntity(huespedDto);
        return huespedRepository.save(huespedGuardar);
    }

    public List<Huesped> buscarHuespedes(HuespedDTO filtro) {
        Specification<Huesped> spec = Specification.unrestricted();

        // --- CORRECCIÓN 1: FILTRAR BORRADOS LÓGICOS ---
        // Solo traemos los que NO están borrados (borradoLogico = false o null)
        spec = spec.and((root, query, cb) -> cb.or(
                cb.isFalse(root.get("borradoLogico")),
                cb.isNull(root.get("borradoLogico"))));
        // ----------------------------------------------

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
        return huespedRepository.findById(id)
                .map(Huesped::isAlojado)
                .orElse(false);
    }

    // --- CORRECCIÓN 2: BORRADO LÓGICO ---
    public void eliminarHuesped(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());

        // En lugar de deleteById, buscamos y actualizamos el flag
        Huesped huesped = huespedRepository.findById(id).orElse(null);
        if (huesped != null) {
            huesped.setBorradoLogico(true);
            huespedRepository.save(huesped);
        }
    }
    // ------------------------------------

    @Transactional
    public void modificarHuesped(HuespedDTO dto, HuespedPK pkAnterior, boolean modificoPK) {

        if (modificoPK) {
            // CASO 1: Cambio de Identidad (PK)
            Huesped huespedAnterior = huespedRepository.findById(pkAnterior).orElse(null);
            if (huespedAnterior != null) {
                huespedAnterior.setBorradoLogico(true);
                huespedRepository.save(huespedAnterior);
            }

            Huesped huespedNuevo = huespedMapper.toEntity(dto);
            huespedNuevo.setBorradoLogico(false);
            huespedRepository.save(huespedNuevo);

        } else {
            // CASO 2: Modificación de datos simples
            HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
            Huesped existente = huespedRepository.findById(id).orElse(null);

            if (existente != null) {
                huespedMapper.updateEntity(existente, dto);
                existente.setBorradoLogico(false);
                huespedRepository.save(existente);
            }
        }
    }

    public Huesped obtenerHuespedPorId(HuespedPK id) {
        return huespedRepository.findById(id).orElse(null);
    }
}