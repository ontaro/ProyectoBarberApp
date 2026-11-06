package com.barberapp.barberapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        SavedRequestAwareAuthenticationSuccessHandler porDefecto =
                new SavedRequestAwareAuthenticationSuccessHandler();
        porDefecto.setDefaultTargetUrl("/");

        AuthenticationSuccessHandler porRol = (request, response, authentication) -> {
            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (esAdmin) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                porDefecto.onAuthenticationSuccess(request, response, authentication);
            }
        };

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/registro", "/login", "/css/**", "/img/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("correo")
                .successHandler(porRol)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout")
                .permitAll()
            );

        return http.build();
    }
}