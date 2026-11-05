package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    public List<Servicio> listarActivos() {
        return servicioRepository.findByActivoTrueOrderByIdAsc();
    }

    public Optional<Servicio> buscarActivo(Long id) {
        return servicioRepository.findByIdAndActivoTrue(id);
    }
}
