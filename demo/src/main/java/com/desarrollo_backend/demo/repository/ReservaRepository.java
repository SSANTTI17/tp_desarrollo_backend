package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import org.springframework.stereotype.Repository;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

       List<Reserva> findByApellido(String apellido);

       List<Reserva> findByApellidoAndNombre(String apellido, String nombre);

       // CORRECCIÓN: Usamos lógica de RANGO.
       // Buscamos una reserva donde la habitación coincida Y la fecha dada esté
       // entre el ingreso y el egreso.
       @Query("SELECT r FROM reservas r JOIN r.habitacionesReservadas h " +
                     "WHERE h.id.numero = :numero " +
                     "AND h.id.tipo = :tipo " +
                     "AND (:fecha >= r.fechaIngreso AND :fecha <= r.fechaEgreso)")
       Reserva ReservasPorHabitacionYFecha(
                     @Param("numero") int numero,
                     @Param("tipo") TipoHabitacion tipo,
                     @Param("fecha") Date fecha);
}