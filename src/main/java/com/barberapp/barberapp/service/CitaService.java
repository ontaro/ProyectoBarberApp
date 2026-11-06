package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private static final ZoneId ZONA = ZoneId.of("America/Costa_Rica");
    private static final int DURACION_MINUTOS = 60;

    private final CitaRepository citaRepository;

    public LocalDate hoy() {
        return LocalDate.now(ZONA);
    }

    @Transactional(readOnly = true)
    public List<LocalTime> horasDisponibles(Barbero barbero, LocalDate fecha) {
        List<LocalTime> disponibles = new ArrayList<>();
        LocalDate hoy = hoy();

        if (fecha.isBefore(hoy)) {
            return disponibles;
        }

        List<LocalTime> ocupadas = citaRepository.horasOcupadas(barbero.getIdBarbero(), fecha);
        LocalTime ahora = LocalTime.now(ZONA);
        LocalTime hora = barbero.getHoraInicio();

        while (!hora.plusMinutes(DURACION_MINUTOS).isAfter(barbero.getHoraFin())) {
            boolean yaPaso = fecha.equals(hoy) && !hora.isAfter(ahora);
            if (!yaPaso && !ocupadas.contains(hora)) {
                disponibles.add(hora);
            }
            hora = hora.plusMinutes(DURACION_MINUTOS);
            if (hora.equals(LocalTime.MIDNIGHT)) {
                break;
            }
        }
        return disponibles;
    }
}