package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollo_backend.demo.modelo.conserje.Conserje;
import java.util.Optional;

@Repository
public interface ConserjeRepository extends JpaRepository<Conserje, Long> {

    Optional<Conserje> findByNombre(String nombre);
}