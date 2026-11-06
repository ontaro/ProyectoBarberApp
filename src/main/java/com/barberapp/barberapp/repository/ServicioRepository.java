package com.barberapp.barberapp.repository;

import com.barberapp.barberapp.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    @Query("SELECT s FROM Servicio s WHERE s.activo = true ORDER BY s.idServicio ASC")
    List<Servicio> listarActivos();
}