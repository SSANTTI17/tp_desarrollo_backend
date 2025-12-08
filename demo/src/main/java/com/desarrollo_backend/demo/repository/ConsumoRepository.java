package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.estadias.Consumo;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Integer> {}