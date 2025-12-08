package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.huesped.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, HuespedPK>, JpaSpecificationExecutor<Huesped> {}
