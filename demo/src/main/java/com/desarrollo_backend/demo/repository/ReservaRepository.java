package com.desarrollo_backend.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Integer>{

    List<Reserva> findByApellido(String apellido);

    List<Reserva> findByApellidoAndNombre(String apellido, String nombre);

}
