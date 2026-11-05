package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ServicioService servicioService;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("servicios", servicioService.listarActivos());
        return "index";
    }
}
