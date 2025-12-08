package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.direccion.Localidad;

@Repository

public interface LocalidadRepository extends JpaRepository<Localidad,Integer> {}
