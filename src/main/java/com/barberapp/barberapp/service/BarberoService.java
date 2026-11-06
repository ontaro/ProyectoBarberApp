package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.repository.BarberoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarberoService {

    private final BarberoRepository barberoRepository;

    @Transactional(readOnly = true)
    public List<Barbero> listarActivos() {
        return barberoRepository.listarActivos();
    }

    @Transactional(readOnly = true)
    public List<Barbero> listarTodos() {
        return barberoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Barbero> buscarActivo(Integer idBarbero) {
        return barberoRepository.findById(idBarbero).filter(Barbero::isActivo);
    }

    @Transactional
    public void guardar(Barbero barbero) {
        barberoRepository.saveAndFlush(barbero);
    }

    @Transactional
    public void eliminar(Integer idBarbero) {
        if (!barberoRepository.existsById(idBarbero)) {
            throw new IllegalArgumentException("El barbero no existe.");
        }
        try {
            barberoRepository.deleteById(idBarbero);
            barberoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("El barbero tiene datos asociados.", e);
        }
    }
}