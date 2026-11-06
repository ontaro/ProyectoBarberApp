package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.service.BarberoService;
import com.barberapp.barberapp.service.CitaService;
import com.barberapp.barberapp.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservar/fecha")
public class SeleccionFechaController {

    private final ServicioService servicioService;
    private final BarberoService barberoService;
    private final CitaService citaService;

    @GetMapping
    public String paso3(@RequestParam(required = false) Integer servicioId,
                        @RequestParam(required = false) Integer barberoId,
                        @RequestParam(required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                        Model model) {

        Optional<Servicio> servicio = (servicioId == null)
                ? Optional.empty() : servicioService.buscarActivo(servicioId);
        if (servicio.isEmpty()) {
            return "redirect:/reservar";
        }

        Optional<Barbero> barbero = (barberoId == null)
                ? Optional.empty() : barberoService.buscarActivo(barberoId);
        if (barbero.isEmpty()) {
            return "redirect:/reservar/barbero?servicioId=" + servicioId;
        }

        model.addAttribute("servicio", servicio.get());
        model.addAttribute("barbero", barbero.get());
        model.addAttribute("hoy", citaService.hoy());

        if (fecha != null) {
            model.addAttribute("fecha", fecha);
            if (fecha.isBefore(citaService.hoy())) {
                model.addAttribute("error", "No puedes elegir una fecha que ya pasó");
            } else {
                model.addAttribute("horas", citaService.horasDisponibles(barbero.get(), fecha));
            }
        }
        return "reservar/fecha";
    }
}