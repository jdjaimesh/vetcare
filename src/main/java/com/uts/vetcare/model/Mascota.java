package com.uts.vetcare.model;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60)
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    @NotBlank(message = "La raza es obligatoria")
    @Size(max = 60)
    private String raza;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "150", message = "El peso máximo es 150 kg")
    private Double peso;

    // RN-01: una mascota pertenece a un solo propietario
    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    /** Método de negocio (sin capa service): edad legible de la mascota. */
    @Transient
    public String getEdad() {
        if (fechaNacimiento == null) return "";
        Period p = Period.between(fechaNacimiento, LocalDate.now());
        if (p.getYears() > 0) {
            return p.getYears() + (p.getYears() == 1 ? " año" : " años");
        }
        return p.getMonths() + (p.getMonths() == 1 ? " mes" : " meses");
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }
}
