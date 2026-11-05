package com.barberapp.barberapp.config;

import com.barberapp.barberapp.domain.Rol;
import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.domain.Usuario;
import com.barberapp.barberapp.repository.ServicioRepository;
import com.barberapp.barberapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

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

    @Bean
    CommandLineRunner crearServicios(ServicioRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                List<String[]> datos = List.of(
                        new String[]{"Corte", "4500"},
                        new String[]{"Barba", "3000"},
                        new String[]{"Cejas", "1000"},
                        new String[]{"Otros", "4000"}
                );
                for (String[] d : datos) {
                    Servicio s = new Servicio();
                    s.setNombre(d[0]);
                    s.setPrecio(new BigDecimal(d[1]));
                    repo.save(s);
                }
            }
        };
    }
}
