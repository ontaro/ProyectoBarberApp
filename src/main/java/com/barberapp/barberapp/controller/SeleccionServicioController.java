package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservar")
public class SeleccionServicioController {

    private final ServicioService servicioService;

    // Paso 1: se puede llegar desde "Reservar cita" (sin servicio)
    // o desde el botón "Reservar" de un servicio (con servicioId)
    @GetMapping
    public String paso1(@RequestParam(required = false) Long servicioId, Model model) {
        model.addAttribute("servicios", servicioService.listarActivos());
        model.addAttribute("servicioSeleccionado", servicioId);
        return "reservar/servicio";
    }

    @PostMapping
    public String continuar(@RequestParam(required = false) Long servicioId, Model model) {
        if (servicioId == null || servicioService.buscarActivo(servicioId).isEmpty()) {
            model.addAttribute("servicios", servicioService.listarActivos());
            model.addAttribute("error", "Debes seleccionar un servicio para continuar");
            return "reservar/servicio";
        }
        // Paso 2 (HU-08, persona C)
        return "redirect:/reservar/barbero?servicioId=" + servicioId;
    }
}
