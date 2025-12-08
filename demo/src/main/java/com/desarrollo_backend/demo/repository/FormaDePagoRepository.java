package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.factura.FormaDePago;

@Repository
public interface FormaDePagoRepository extends JpaRepository<FormaDePago,Integer>{}
