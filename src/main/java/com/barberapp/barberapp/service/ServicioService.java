package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Servicio> listarActivos() {
        return servicioRepository.listarActivos();
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> buscarActivo(Integer idServicio) {
        return servicioRepository.findById(idServicio).filter(Servicio::isActivo);
    }

    @Transactional
    public void guardar(Servicio servicio) {
        servicioRepository.saveAndFlush(servicio);
    }

    @Transactional
    public void eliminar(Integer idServicio) {
        if (!servicioRepository.existsById(idServicio)) {
            throw new IllegalArgumentException("El servicio no existe.");
        }
        try {
            servicioRepository.deleteById(idServicio);
            servicioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("El servicio tiene datos asociados.", e);
        }
    }
}