package com.uts.vetcare.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uts.vetcare.model.Rol;
import com.uts.vetcare.model.Usuario;
import com.uts.vetcare.repository.PropietarioRepository;
import com.uts.vetcare.repository.UsuarioRepository;

import jakarta.validation.Valid;

/** HU-01: el administrador crea usuarios y les asigna un rol. */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PropietarioRepository propietarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PropietarioRepository propietarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.propietarioRepository = propietarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        cargarCombos(model);
        return "usuarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return usuarioRepository.findById(id).map(u -> {
            u.setPassword(null); // nunca se envía el hash a la vista
            model.addAttribute("usuario", u);
            cargarCombos(model);
            return "usuarios/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "El usuario no existe");
            return "redirect:/usuarios";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult br,
                          @RequestParam(required = false) String password,
                          Model model, RedirectAttributes ra) {

        // El select envía propietario.id vacío cuando no se elige nada
        if (usuario.getPropietario() != null && usuario.getPropietario().getId() == null) {
            usuario.setPropietario(null);
        }

        if (usuario.getRol() == Rol.PROPIETARIO && usuario.getPropietario() == null) {
            br.rejectValue("propietario", "requerido", "Selecciona el propietario asociado a este usuario");
        }
        if (usuario.getRol() != null && usuario.getRol() != Rol.PROPIETARIO) {
            usuario.setPropietario(null);
        }

        usuarioRepository.findByUsername(usuario.getUsername()).ifPresent(existente -> {
            if (!existente.getId().equals(usuario.getId())) {
                br.rejectValue("username", "duplicado", "Ese nombre de usuario ya existe");
            }
        });

        boolean claveVacia = password == null || password.isBlank();
        if (usuario.getId() == null && claveVacia) {
            br.rejectValue("password", "requerido", "La contraseña es obligatoria");
        } else if (!claveVacia && password.length() < 6) {
            br.rejectValue("password", "corta", "La contraseña debe tener mínimo 6 caracteres");
        }

        if (br.hasErrors()) {
            cargarCombos(model);
            return "usuarios/form";
        }

        if (!claveVacia) {
            usuario.setPassword(passwordEncoder.encode(password));
        } else {
            // edición sin cambio de contraseña: se conserva la actual
            usuario.setPassword(usuarioRepository.findById(usuario.getId()).orElseThrow().getPassword());
        }

        usuarioRepository.save(usuario);
        ra.addFlashAttribute("mensaje", "Usuario guardado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        usuarioRepository.findById(id).ifPresent(u -> {
            if (u.getUsername().equals(auth.getName())) {
                ra.addFlashAttribute("error", "No puedes eliminar tu propio usuario");
            } else {
                usuarioRepository.delete(u);
                ra.addFlashAttribute("mensaje", "Usuario eliminado");
            }
        });
        return "redirect:/usuarios";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("roles", Rol.values());
        model.addAttribute("propietarios", propietarioRepository.findAll());
    }
}
