package com.uts.vetcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uts.vetcare.repository.MascotaRepository;
import com.uts.vetcare.repository.PropietarioRepository;
import com.uts.vetcare.repository.UsuarioRepository;

@Controller
public class HomeController {

    private final UsuarioRepository usuarioRepository;
    private final PropietarioRepository propietarioRepository;
    private final MascotaRepository mascotaRepository;

    public HomeController(UsuarioRepository usuarioRepository, PropietarioRepository propietarioRepository,
                          MascotaRepository mascotaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.propietarioRepository = propietarioRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalPropietarios", propietarioRepository.count());
        model.addAttribute("totalMascotas", mascotaRepository.count());
        return "index";
    }
}
