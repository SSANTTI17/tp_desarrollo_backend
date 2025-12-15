package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.desarrollo_backend.demo.modelo.estadias.Estadia;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {
    List<Estadia> findByFechaFin(Date fechaFin);

    @Query("SELECT e FROM Estadia e WHERE e.habitacion.id.numero = :numero AND e.habitacion.id.tipo = :tipo AND e.fechaFin = :fechaFin")
    Optional<Estadia> buscarPorHabitacionYFechaFin(
            @Param("numero") int numero,
            @Param("tipo") TipoHabitacion tipo,
            @Param("fechaFin") Date fechaFin);
}