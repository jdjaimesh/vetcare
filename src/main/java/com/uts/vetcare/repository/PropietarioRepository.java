package com.uts.vetcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uts.vetcare.model.Propietario;

public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Optional<Propietario> findByDocumento(String documento);

    @Query("SELECT p FROM Propietario p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) "
         + "OR p.documento LIKE CONCAT('%', :q, '%') ORDER BY p.nombre")
    List<Propietario> buscar(@Param("q") String q);
}
