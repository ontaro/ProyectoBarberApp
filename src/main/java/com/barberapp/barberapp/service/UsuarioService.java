package com.barberapp.barberapp.service;

import com.barberapp.barberapp.domain.RegistroForm;
import com.barberapp.barberapp.domain.Rol;
import com.barberapp.barberapp.domain.Usuario;
import com.barberapp.barberapp.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo.trim().toLowerCase());
    }

    public Usuario registrar(RegistroForm form) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(form.getNombreCompleto().trim());
        usuario.setCorreo(form.getCorreo().trim().toLowerCase());
        usuario.setTelefono(form.getTelefono().trim());
        usuario.setContrasena(passwordEncoder.encode(form.getContrasena()));
        usuario.setRol(Rol.CLIENTE);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo.trim().toLowerCase());
    }
}