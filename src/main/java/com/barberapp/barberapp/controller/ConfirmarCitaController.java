package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.domain.Cita;
import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.domain.Usuario;
import com.barberapp.barberapp.service.BarberoService;
import com.barberapp.barberapp.service.CitaService;
import com.barberapp.barberapp.service.ServicioService;
import com.barberapp.barberapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservar")
public class ConfirmarCitaController {

    private final ServicioService servicioService;
    private final BarberoService barberoService;
    private final UsuarioService usuarioService;
    private final CitaService citaService;

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam Integer servicioId,
                            @RequestParam Integer barberoId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora,
                            Authentication authentication,
                            Model model,
                            RedirectAttributes redirect) {

        Optional<Servicio> servicio = servicioService.buscarActivo(servicioId);
        Optional<Barbero> barbero = barberoService.buscarActivo(barberoId);
        Optional<Usuario> cliente = usuarioService.buscarPorCorreo(authentication.getName());

        if (servicio.isEmpty() || barbero.isEmpty() || cliente.isEmpty()) {
            return "redirect:/reservar";
        }

        if (!citaService.horasDisponibles(barbero.get(), fecha).contains(hora)) {
            redirect.addFlashAttribute("error", "La hora seleccionada ya no está disponible. Elige otra.");
            return "redirect:/reservar/fecha?servicioId=" + servicioId
                    + "&barberoId=" + barberoId + "&fecha=" + fecha;
        }

        model.addAttribute("servicio", servicio.get());
        model.addAttribute("barbero", barbero.get());
        model.addAttribute("cliente", cliente.get());
        model.addAttribute("fecha", fecha);
        model.addAttribute("hora", hora);
        return "reservar/confirmar";
    }

    @PostMapping("/finalizar")
    public String finalizar(@RequestParam Integer servicioId,
                            @RequestParam Integer barberoId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora,
                            Authentication authentication,
                            RedirectAttributes redirect) {

        Optional<Servicio> servicio = servicioService.buscarActivo(servicioId);
        Optional<Barbero> barbero = barberoService.buscarActivo(barberoId);
        Optional<Usuario> cliente = usuarioService.buscarPorCorreo(authentication.getName());

        if (servicio.isEmpty() || barbero.isEmpty() || cliente.isEmpty()) {
            return "redirect:/reservar";
        }

        try {
            Cita cita = citaService.reservar(cliente.get(), servicio.get(), barbero.get(), fecha, hora);
            return "redirect:/reservar/exito?citaId=" + cita.getIdCita();
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", "La hora seleccionada ya no está disponible. Elige otra.");
            return "redirect:/reservar/fecha?servicioId=" + servicioId
                    + "&barberoId=" + barberoId + "&fecha=" + fecha;
        }
    }

    @GetMapping("/exito")
    public String exito(@RequestParam Long citaId, Authentication authentication, Model model) {
        Optional<Cita> cita = citaService.buscarDeCliente(citaId, authentication.getName());
        if (cita.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("cita", cita.get());
        return "reservar/exito";
    }
}