package com.barberapp.barberapp.repository;

import com.barberapp.barberapp.domain.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c.hora FROM Cita c "
            + "WHERE c.barbero.idBarbero = :idBarbero "
            + "AND c.fecha = :fecha "
            + "AND c.estado <> com.barberapp.barberapp.domain.EstadoCita.CANCELADA")
    List<LocalTime> horasOcupadas(@Param("idBarbero") Integer idBarbero,
            @Param("fecha") LocalDate fecha);

    @Query("""
        SELECT c FROM Cita c
        JOIN FETCH c.servicio
        JOIN FETCH c.barbero
        WHERE c.cliente.correo = :correo
          AND (
              c.fecha > :fecha
              OR (c.fecha = :fecha AND c.hora > :hora)
          )
          AND c.estado <> com.barberapp.barberapp.domain.EstadoCita.CANCELADA
        ORDER BY c.fecha ASC, c.hora ASC
        """)
    List<Cita> buscarCitasFuturas(
            @Param("correo") String correo,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );
}
