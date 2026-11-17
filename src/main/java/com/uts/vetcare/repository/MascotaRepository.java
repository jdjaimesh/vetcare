package com.uts.vetcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uts.vetcare.model.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByPropietarioId(Long propietarioId);
    boolean existsByPropietarioId(Long propietarioId);
}
