package com.uts.vetcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uts.vetcare.model.Propietario;
import com.uts.vetcare.repository.MascotaRepository;
import com.uts.vetcare.repository.PropietarioRepository;
import com.uts.vetcare.repository.UsuarioRepository;

import jakarta.validation.Valid;

/** HU-02: la recepcionista registra propietarios. */
@Controller
@RequestMapping("/propietarios")
public class PropietarioController {

    private final PropietarioRepository propietarioRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public PropietarioController(PropietarioRepository propietarioRepository, MascotaRepository mascotaRepository,
                                 UsuarioRepository usuarioRepository) {
        this.propietarioRepository = propietarioRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q, Model model) {
        boolean hayBusqueda = q != null && !q.isBlank();
        model.addAttribute("propietarios",
                hayBusqueda ? propietarioRepository.buscar(q.trim()) : propietarioRepository.findAll());
        model.addAttribute("q", q);
        return "propietarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("propietario", new Propietario());
        return "propietarios/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return propietarioRepository.findById(id).map(p -> {
            model.addAttribute("propietario", p);
            return "propietarios/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "El propietario no existe");
            return "redirect:/propietarios";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("propietario") Propietario propietario, BindingResult br,
                          RedirectAttributes ra) {

        propietarioRepository.findByDocumento(propietario.getDocumento()).ifPresent(existente -> {
            if (!existente.getId().equals(propietario.getId())) {
                br.rejectValue("documento", "duplicado", "Ya existe un propietario con ese documento");
            }
        });

        if (br.hasErrors()) {
            return "propietarios/form";
        }
        propietarioRepository.save(propietario);
        ra.addFlashAttribute("mensaje", "Propietario guardado correctamente");
        return "redirect:/propietarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (mascotaRepository.existsByPropietarioId(id)) {
            ra.addFlashAttribute("error", "No se puede eliminar: el propietario tiene mascotas registradas");
        } else if (usuarioRepository.existsByPropietarioId(id)) {
            ra.addFlashAttribute("error", "No se puede eliminar: el propietario tiene un usuario de acceso asociado");
        } else {
            propietarioRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Propietario eliminado");
        }
        return "redirect:/propietarios";
    }
}
