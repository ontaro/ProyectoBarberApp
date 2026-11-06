package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MisCitasController {

    private final CitaService citaService;

    @GetMapping("/mis-citas")
    public String misCitas(Authentication authentication, Model model) {

        var citas = citaService.obtenerCitasFuturas(
                authentication.getName()
        );

        model.addAttribute("citas", citas);

        return "reservar/mis-citas";
    }
}