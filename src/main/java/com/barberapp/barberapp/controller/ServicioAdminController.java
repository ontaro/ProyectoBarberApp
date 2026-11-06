package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.domain.Servicio;
import com.barberapp.barberapp.service.ServicioService;
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
@RequestMapping("/admin/servicios")
public class ServicioAdminController {

    private final ServicioService servicioService;

    @GetMapping
    public String listado(Model model) {
        model.addAttribute("servicios", servicioService.listarTodos());
        return "admin/servicios";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Servicio servicio, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("error", "Revise los datos: el nombre es obligatorio y el precio debe ser mayor a 0.");
        } else {
            try {
                servicioService.guardar(servicio);
                redirect.addFlashAttribute("todoOk", "Registro actualizado correctamente.");
            } catch (DataIntegrityViolationException e) {
                redirect.addFlashAttribute("error", "Ya existe un servicio con ese nombre.");
            }
        }
        return "redirect:/admin/servicios";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idServicio, RedirectAttributes redirect) {
        try {
            servicioService.eliminar(idServicio);
            redirect.addFlashAttribute("todoOk", "Registro eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", "El servicio no existe.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", "No se puede eliminar: el servicio tiene datos asociados.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "No fue posible eliminar el registro.");
        }
        return "redirect:/admin/servicios";
    }
}