# ADR-001 — Sin capa `service`

- **Estado:** Aceptada
- **Contexto:** El parcial prohíbe la carpeta `service`.
- **Decisión:** Los controladores usan directamente los repositorios JPA. La lógica de negocio va en controladores, consultas `@Query` y métodos de las entidades (por ejemplo `Mascota.getEdad()`).
- **Consecuencias:** Menos clases, pero los controladores concentran validaciones. Se evita duplicar lógica extrayéndola a métodos de entidad o `@Query` cuando se repite.
