package com.barberapp.barberapp.repository;

import com.barberapp.barberapp.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByActivoTrueOrderByIdAsc();

    Optional<Servicio> findByIdAndActivoTrue(Long id);
}
