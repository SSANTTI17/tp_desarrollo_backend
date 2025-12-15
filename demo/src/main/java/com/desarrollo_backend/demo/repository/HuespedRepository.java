package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, HuespedPK>, JpaSpecificationExecutor<Huesped> {
    @Query("SELECT DISTINCT h FROM Huesped h JOIN h.reservasAsociadas r WHERE r = :reserva")
    List<Huesped> findByReservas(@Param("reserva") Reserva reserva);
}
