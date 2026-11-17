# ADR-003 — `UserDetailsService` como bean de configuración

- **Estado:** Aceptada
- **Contexto:** Spring Security necesita un `UserDetailsService`, pero no puede existir una clase en una capa `service`.
- **Decisión:** Se define como `@Bean` (expresión lambda) dentro de `config/SecurityConfig`, usando `UsuarioRepository`.
- **Consecuencias:** Cumple la restricción sin perder la autenticación contra base de datos.
