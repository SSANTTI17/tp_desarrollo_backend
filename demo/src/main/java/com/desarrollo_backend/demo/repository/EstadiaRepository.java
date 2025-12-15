package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {
    List<Estadia> findByFechaFin(Date fechaFin);
}