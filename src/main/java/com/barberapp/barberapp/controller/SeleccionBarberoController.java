package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.service.BarberoService;
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
@RequestMapping("/reservar/barbero")
public class SeleccionBarberoController {

    private final BarberoService barberoService;
    private final ServicioService servicioService;

    @GetMapping
    public String paso2(@RequestParam(required = false) Integer servicioId, Model model) {
        // Si no hay un servicio válido, se regresa al paso 1
        if (servicioId == null || servicioService.buscarActivo(servicioId).isEmpty()) {
            return "redirect:/reservar";
        }
        model.addAttribute("servicioId", servicioId);
        model.addAttribute("servicio", servicioService.buscarActivo(servicioId).get());
        model.addAttribute("barberos", barberoService.listarActivos());
        return "reservar/barbero";
    }

    @PostMapping
    public String continuar(@RequestParam Integer servicioId,
                            @RequestParam(required = false) Integer barberoId,
                            Model model) {
        if (servicioService.buscarActivo(servicioId).isEmpty()) {
            return "redirect:/reservar";
        }
        if (barberoId == null || barberoService.buscarActivo(barberoId).isEmpty()) {
            model.addAttribute("servicioId", servicioId);
            model.addAttribute("servicio", servicioService.buscarActivo(servicioId).get());
            model.addAttribute("barberos", barberoService.listarActivos());
            model.addAttribute("error", "Debes seleccionar un barbero para continuar");
            return "reservar/barbero";
        }
        return "redirect:/reservar/fecha?servicioId=" + servicioId + "&barberoId=" + barberoId;
    }
}