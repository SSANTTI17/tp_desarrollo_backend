package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.estadias.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {}