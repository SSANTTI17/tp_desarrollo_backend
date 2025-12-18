package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {
    List<Estadia> findByFechaFin(Date fechaFin);

    Optional<Estadia> findByReserva(Reserva reserva);

    // CORRECCIÓN:
    // 1. Usamos 'e.habitacion.id.numero' y 'e.habitacion.id.tipo' porque es una
    // clave compuesta.
    // 2. Unificamos los nombres de los parámetros (:numero, :tipo, :fecha).
    // 3. La lógica busca si la fecha ingresada cae DENTRO del rango [inicio, fin]
    // de la estadía.
    @Query("SELECT e FROM Estadia e WHERE e.habitacion.id.numero = :numero " +
          "AND e.habitacion.id.tipo = :tipo " +
          "AND :fecha >= e.fechaInicio " +
          "AND :fecha <= e.fechaFin " +
          "AND e.facturadaEstadia = false")
    Optional<Estadia> buscarPorHabitacionYFechaFin(
          @Param("numero") int numero,
          @Param("tipo") TipoHabitacion tipo,
          @Param("fecha") Date fecha);
 }