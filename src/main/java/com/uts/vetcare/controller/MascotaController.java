package com.uts.vetcare.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uts.vetcare.model.Mascota;
import com.uts.vetcare.model.Rol;
import com.uts.vetcare.model.Usuario;
import com.uts.vetcare.repository.MascotaRepository;
import com.uts.vetcare.repository.PropietarioRepository;
import com.uts.vetcare.repository.UsuarioRepository;

import jakarta.validation.Valid;

/** HU-02: registro de mascotas. RN-08: el propietario solo ve sus propias mascotas. */
@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    private static final List<String> ESPECIES = List.of("Perro", "Gato", "Ave", "Conejo", "Otro");
    private static final List<String> SEXOS = List.of("Macho", "Hembra");

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;
    private final UsuarioRepository usuarioRepository;

    public MascotaController(MascotaRepository mascotaRepository, PropietarioRepository propietarioRepository,
                             UsuarioRepository usuarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.propietarioRepository = propietarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long propietarioId, Authentication auth, Model model) {
        Usuario actual = usuarioRepository.findByUsername(auth.getName()).orElseThrow();
        List<Mascota> mascotas;

        if (actual.getRol() == Rol.PROPIETARIO) {
            mascotas = actual.getPropietario() == null
                    ? List.of()
                    : mascotaRepository.findByPropietarioId(actual.getPropietario().getId());
        } else if (propietarioId != null) {
            mascotas = mascotaRepository.findByPropietarioId(propietarioId);
        } else {
            mascotas = mascotaRepository.findAll();
        }
        model.addAttribute("mascotas", mascotas);
        return "mascotas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) Long propietarioId, Model model) {
        Mascota m = new Mascota();
        if (propietarioId != null) {
            propietarioRepository.findById(propietarioId).ifPresent(m::setPropietario);
        }
        model.addAttribute("mascota", m);
        cargarCombos(model);
        return "mascotas/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return mascotaRepository.findById(id).map(m -> {
            model.addAttribute("mascota", m);
            cargarCombos(model);
            return "mascotas/form";
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "La mascota no existe");
            return "redirect:/mascotas";
        });
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("mascota") Mascota mascota, BindingResult br,
                          Model model, RedirectAttributes ra) {

        // El select envía propietario.id vacío cuando no se elige nada
        if (mascota.getPropietario() == null || mascota.getPropietario().getId() == null) {
            mascota.setPropietario(null);
            br.rejectValue("propietario", "requerido", "Selecciona el propietario");
        } else if (!propietarioRepository.existsById(mascota.getPropietario().getId())) {
            br.rejectValue("propietario", "invalido", "El propietario seleccionado no existe");
        }

        if (br.hasErrors()) {
            cargarCombos(model);
            return "mascotas/form";
        }
        mascotaRepository.save(mascota);
        ra.addFlashAttribute("mensaje", "Mascota guardada correctamente");
        return "redirect:/mascotas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        mascotaRepository.deleteById(id);
        ra.addFlashAttribute("mensaje", "Mascota eliminada");
        return "redirect:/mascotas";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("propietarios", propietarioRepository.findAll());
        model.addAttribute("especies", ESPECIES);
        model.addAttribute("sexos", SEXOS);
    }
}
