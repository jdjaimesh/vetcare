# ADR-004 — Relación Mascota → Propietario unidireccional

- **Estado:** Aceptada
- **Contexto:** Con `open-in-view=false`, una colección perezosa en `Propietario` provocaría errores en las vistas.
- **Decisión:** Solo `Mascota` y `Usuario` referencian a `Propietario`. Las comprobaciones de dependencias usan `existsByPropietarioId` en los repositorios.
- **Consecuencias:** No hay carga perezosa en las vistas y las consultas son explícitas.
