package com.barberapp.barberapp.controller;

import com.barberapp.barberapp.domain.RegistroForm;
import com.barberapp.barberapp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class RegistroController {

    private final UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult result,
                            RedirectAttributes redirect) {

        if (!Objects.equals(form.getContrasena(), form.getConfirmarContrasena())) {
            result.rejectValue("confirmarContrasena", "error.confirmar",
                    "Las contraseñas no coinciden");
        }

        if (!result.hasFieldErrors("correo") && usuarioService.existeCorreo(form.getCorreo())) {
            result.rejectValue("correo", "error.correo", "Este correo ya está registrado");
        }

        if (result.hasErrors()) {
            return "registro";
        }

        usuarioService.registrar(form);
        redirect.addFlashAttribute("exito", "Registro exitoso. Pronto podrás iniciar sesión.");
        return "redirect:/";
    }
}