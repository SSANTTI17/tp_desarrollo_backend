package com.desarrollo_backend.demo.gestores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.repository.HuespedRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio encargado de la lógica de negocio referente al Caso de Uso 04
 * (CU04):
 * Gestión de Huéspedes.
 * Provee métodos para alta, baja, modificación y consulta dinámica.
 */
@Service
public class GestorHuesped {

    @Autowired
    private HuespedRepository huespedRepository;

    /**
     * Da de alta un nuevo huésped en el sistema.
     * * @param huespedDto Objeto con los datos del nuevo huésped.
     * 
     * @return La entidad {@link Huesped} guardada en la base de datos, o null si el
     *         input es nulo.
     */

    public Huesped darDeAltaHuesped(HuespedDTO huespedDto) {
        if (huespedDto == null)
            return null;

        Huesped huespedGuardar = new Huesped(huespedDto);
        return huespedRepository.save(huespedGuardar);
    }

    /**
     * Busca huéspedes aplicando filtros dinámicos mediante Specifications.
     * Permite filtrar por apellido, nombre, número y tipo de documento de forma
     * combinada.
     * * @param filtro DTO que contiene los criterios de búsqueda (puede tener
     * campos nulos).
     * 
     * @return Lista de huéspedes que coinciden con los criterios proporcionados.
     */
    public List<Huesped> buscarHuespedes(HuespedDTO filtro) {
        Specification<Huesped> spec = Specification.unrestricted(); // base vacía
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

    /**
     * Convierte una entidad Huesped a su representación DTO.
     * * @param entidad La entidad a convertir.
     * 
     * @return El objeto DTO correspondiente.
     */
    private HuespedDTO convertirADTO(Huesped entidad) {
        HuespedDTO dto = new HuespedDTO(entidad);
        return dto;
    }

    /**
     * Verifica si un huésped se encuentra actualmente registrado como alojado.
     * * @param dto DTO con la clave primaria del huésped.
     * 
     * @return true si el huésped está marcado como alojado, false en caso
     *         contrario.
     */
    public boolean huespedIsAlojado(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        Huesped huesped = huespedRepository.findById(id).get();
        return huesped.isAlojado();
    }

    /**
     * Elimina físicamente un huésped de la base de datos dado su ID.
     * * @param dto DTO con la clave primaria del huésped a eliminar.
     */
    public void eliminarHuesped(HuespedDTO dto) {
        HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        huespedRepository.deleteById(id);
    }

    /**
     * Modifica los datos de un huésped existente.
     * Maneja dos escenarios:
     * 1. Si se modificó la clave primaria (Documento): Realiza un borrado lógico
     * del anterior y crea uno nuevo.
     * 2. Si es una modificación simple: Actualiza los datos sobre el registro
     * existente.
     * * @param dto Datos nuevos del huésped.
     * 
     * @param pkAnterior La clave primaria original antes de la edición.
     * @param modificoPK Flag booleano que indica si hubo cambio de documento (ID).
     */
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

            // B. Crear el nuevo huésped
            Huesped huespedNuevo = new Huesped(dto);
            huespedNuevo.setBorradoLogico(false);
            huespedRepository.save(huespedNuevo);

        } else {

            // CASO 2: Modificación de datos simples (Nombre, mail, etc)
            HuespedPK id = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
            Huesped existente = huespedRepository.findById(id).orElse(null);

            if (existente != null) {
                existente.setHuesped(dto); // Actualiza campos básicos
                existente.setDireccion(dto.getDireccion());
                existente.setBorradoLogico(false); // Aseguramos que no esté borrado
                huespedRepository.save(existente);
            }
        }
    }

    /**
     * Obtiene un huésped por su clave primaria compuesta.
     * * @param id Objeto {@link HuespedPK} con tipo y número de documento.
     * 
     * @return El huésped encontrado o null si no existe.
     */
    public Huesped obtenerHuespedPorId(HuespedPK id) {
        return huespedRepository.findById(id)
                .orElse(null); // (new RuntimeException("El huésped no existe."))
    }

    /**
     * Busca todos los huéspedes asociados a una reserva específica.
     * * @param reserva La reserva por la cual filtrar.
     * 
     * @return Lista de huéspedes asociados a esa reserva.
     */
    public List<Huesped> buscarPorReservas(Reserva reserva) {
        if (reserva == null) {
            return new ArrayList<>();
        }
        return huespedRepository.findByReservas(reserva);
    }

}
