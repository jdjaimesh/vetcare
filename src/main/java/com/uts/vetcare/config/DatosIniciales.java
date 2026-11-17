package com.uts.vetcare.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uts.vetcare.model.*;
import com.uts.vetcare.repository.*;

/** Carga datos de prueba solo si la base está vacía (10-15+ registros para la demo). */
@Configuration
public class DatosIniciales {

    private static final String CLAVE_DEMO = "Vet2026*";

    @Bean
    CommandLineRunner cargarDatos(UsuarioRepository usuarios, PropietarioRepository propietarios,
                                  MascotaRepository mascotas, PasswordEncoder encoder) {
        return args -> {
            if (usuarios.count() > 0) return;

            Propietario p1 = propietario(propietarios, "Laura Martínez", "1098765401", "3001234567", "laura.martinez@correo.com", "Cra 27 #45-12, Bucaramanga");
            Propietario p2 = propietario(propietarios, "Carlos Rojas", "1098765402", "3109876543", "carlos.rojas@correo.com", "Calle 36 #20-30, Bucaramanga");
            Propietario p3 = propietario(propietarios, "Andrea Gómez", "1098765403", "3157654321", "andrea.gomez@correo.com", "Cl 105 #23-10, Floridablanca");
            Propietario p4 = propietario(propietarios, "Julián Pérez", "1098765404", "3201112233", "julian.perez@correo.com", "Cra 15 #60-08, Bucaramanga");
            Propietario p5 = propietario(propietarios, "Marcela Díaz", "1098765405", "3224445566", "marcela.diaz@correo.com", "Cra 9 #12-45, Girón");

            mascota(mascotas, "Max", "Perro", "Golden Retriever", "Macho", LocalDate.of(2020, 3, 12), 30.5, p1);
            mascota(mascotas, "Luna", "Gato", "Siamés", "Hembra", LocalDate.of(2022, 7, 1), 4.2, p1);
            mascota(mascotas, "Rocky", "Perro", "Bulldog Francés", "Macho", LocalDate.of(2021, 11, 20), 12.8, p2);
            mascota(mascotas, "Nala", "Perro", "Labrador", "Hembra", LocalDate.of(2019, 5, 5), 27.0, p3);
            mascota(mascotas, "Michi", "Gato", "Criollo", "Macho", LocalDate.of(2023, 1, 15), 3.9, p3);
            mascota(mascotas, "Toby", "Perro", "Beagle", "Macho", LocalDate.of(2018, 9, 30), 13.4, p4);
            mascota(mascotas, "Kiwi", "Ave", "Periquito", "Hembra", LocalDate.of(2024, 2, 10), 0.4, p5);
            mascota(mascotas, "Copito", "Conejo", "Cabeza de León", "Macho", LocalDate.of(2023, 8, 8), 1.8, p5);

            usuario(usuarios, encoder, "admin", "Administrador VetCare", Rol.ADMIN, null);
            usuario(usuarios, encoder, "veterinario", "Dra. Sofía Ardila", Rol.VETERINARIO, null);
            usuario(usuarios, encoder, "recepcion", "Camila Torres", Rol.RECEPCIONISTA, null);
            usuario(usuarios, encoder, "auxiliar", "Daniel Mejía", Rol.AUXILIAR, null);
            usuario(usuarios, encoder, "laura", "Laura Martínez", Rol.PROPIETARIO, p1);
        };
    }

    private Propietario propietario(PropietarioRepository repo, String nombre, String doc, String tel, String correo, String dir) {
        Propietario p = new Propietario();
        p.setNombre(nombre);
        p.setDocumento(doc);
        p.setTelefono(tel);
        p.setCorreo(correo);
        p.setDireccion(dir);
        return repo.save(p);
    }

    private void mascota(MascotaRepository repo, String nombre, String especie, String raza, String sexo,
                         LocalDate nacimiento, double peso, Propietario dueno) {
        Mascota m = new Mascota();
        m.setNombre(nombre);
        m.setEspecie(especie);
        m.setRaza(raza);
        m.setSexo(sexo);
        m.setFechaNacimiento(nacimiento);
        m.setPeso(peso);
        m.setPropietario(dueno);
        repo.save(m);
    }

    private void usuario(UsuarioRepository repo, PasswordEncoder encoder, String username, String nombre,
                         Rol rol, Propietario propietario) {
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setNombre(nombre);
        u.setPassword(encoder.encode(CLAVE_DEMO));
        u.setRol(rol);
        u.setPropietario(propietario);
        repo.save(u);
    }
}
