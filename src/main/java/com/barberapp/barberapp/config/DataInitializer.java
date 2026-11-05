package com.barberapp.barberapp.config;

import com.barberapp.barberapp.domain.Rol;
import com.barberapp.barberapp.domain.Usuario;
import com.barberapp.barberapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            String correo = "admin@barberapp.com";
            if (!repo.existsByCorreo(correo)) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Administrador");
                admin.setCorreo(correo);
                admin.setTelefono("00000000");
                admin.setContrasena(encoder.encode("Admin12345"));
                admin.setRol(Rol.ADMIN);
                repo.save(admin);
            }
        };
    }
}
