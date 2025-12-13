package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo_backend.demo.modelo.habitacion.*;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, HabitacionPK> {

    Habitacion findByIdNumeroAndIdTipo(int numero, TipoHabitacion tipo);

    default Habitacion findByNumeroAndTipo(int numero, TipoHabitacion tipo) {
        return findByIdNumeroAndIdTipo(numero, tipo);
    }
}
