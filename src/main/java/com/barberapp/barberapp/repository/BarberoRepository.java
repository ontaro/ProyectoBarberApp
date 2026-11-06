package com.barberapp.barberapp.repository;

import com.barberapp.barberapp.domain.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BarberoRepository extends JpaRepository<Barbero, Integer> {

    @Query("SELECT b FROM Barbero b WHERE b.activo = true ORDER BY b.nombre ASC")
    List<Barbero> listarActivos();
}