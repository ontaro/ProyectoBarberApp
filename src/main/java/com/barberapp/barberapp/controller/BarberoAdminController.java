package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.domain.Barbero;
import com.barberapp.barberapp.service.BarberoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/barberos")
public class BarberoAdminController {

    private final BarberoService barberoService;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("barberos", barberoService.listarTodos());
        return "admin/barberos";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Barbero barbero, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Revise los datos: el nombre y el horario son obligatorios.");
        } else if (!barbero.getHoraFin().isAfter(barbero.getHoraInicio())) {
            redirect.addFlashAttribute("error", "La hora de fin debe ser posterior a la hora de inicio.");
        } else {
            try {
                barberoService.guardar(barbero);
                redirect.addFlashAttribute("todoOk", "Registro actualizado correctamente.");
            } catch (DataIntegrityViolationException e) {
                redirect.addFlashAttribute("error", "No fue posible guardar los datos.");
            }
        }
        return "redirect:/admin/barberos";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idBarbero, RedirectAttributes redirect) {
        try {
            barberoService.eliminar(idBarbero);
            redirect.addFlashAttribute("todoOk", "Registro eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", "El barbero no existe.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", "No se puede eliminar: el barbero tiene datos asociados.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "No fue posible eliminar el registro.");
        }
        return "redirect:/admin/barberos";
    }
}