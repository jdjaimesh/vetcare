package com.uts.vetcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uts.vetcare.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByPropietarioId(Long propietarioId);
}
