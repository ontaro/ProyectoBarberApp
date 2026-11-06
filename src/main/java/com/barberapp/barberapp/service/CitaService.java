package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.domain.Cita;
import com.barberapp.barberapp.domain.EstadoCita;
import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.domain.Usuario;
import com.barberapp.barberapp.repository.CitaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitaService {

    private static final ZoneId ZONA = ZoneId.of("America/Costa_Rica");
    private static final int DURACION_MINUTOS = 60;

    private final CitaRepository citaRepository;
    private final EntityManager entityManager;

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

    @Transactional
    public Cita reservar(Usuario cliente, Servicio servicio, Barbero barbero,
                         LocalDate fecha, LocalTime hora) {
        // Se bloquea al barbero: si dos clientes reservan a la vez,
        // el segundo espera, vuelve a revisar y encuentra la hora ocupada.
        entityManager.find(Barbero.class, barbero.getIdBarbero(), LockModeType.PESSIMISTIC_WRITE);

        if (!horasDisponibles(barbero, fecha).contains(hora)) {
            throw new IllegalStateException("La hora seleccionada ya no está disponible.");
        }

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setBarbero(barbero);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado(EstadoCita.RESERVADA);
        return citaRepository.save(cita);
    }

    @Transactional(readOnly = true)
    public Optional<Cita> buscarDeCliente(Long idCita, String correo) {
        return citaRepository.findById(idCita)
                .filter(c -> c.getCliente().getCorreo().equals(correo));
    }
}