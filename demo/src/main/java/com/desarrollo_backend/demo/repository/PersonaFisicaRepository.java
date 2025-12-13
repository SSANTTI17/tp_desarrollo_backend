package com.desarrollo_backend.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.responsablePago.PersonaFisica;

@Repository
public interface PersonaFisicaRepository extends JpaRepository<PersonaFisica, String> {
    Optional<PersonaFisica> findByRefHuesped(Huesped huesped);
}
