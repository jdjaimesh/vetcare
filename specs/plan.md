# Plan técnico — Sprint 1 (VetCare Santander)

> Fase 4 de SDD. Aprobación: **Puerta 2 (Developers)**.

## 1. Arquitectura MVC sin capa service

```
com.uts.vetcare
├── VetcareApplication
├── config/       SecurityConfig · DatosIniciales
├── controller/   HomeController · UsuarioController · PropietarioController · MascotaController
├── model/        Rol · Usuario · Propietario · Mascota
└── repository/   UsuarioRepository · PropietarioRepository · MascotaRepository
resources/templates/  fragments · usuarios · propietarios · mascotas · login · index
```

## 2. Modelo de datos (MySQL)

```
usuarios (id PK, username UNIQUE, nombre, password, rol, activo, propietario_id FK NULL)
propietarios (id PK, nombre, documento UNIQUE, telefono, correo, direccion)
mascotas (id PK, nombre, especie, raza, sexo, fecha_nacimiento, peso, propietario_id FK NOT NULL)
```

Relaciones: `Mascota N—1 Propietario`; `Usuario N—1 Propietario` (opcional, solo rol PROPIETARIO).
Las tablas las crea Hibernate (`ddl-auto=update`).

## 3. Seguridad

- `SecurityFilterChain` con reglas por ruta y rol (ver matriz en `spec.md`).
- `UserDetailsService` como bean en `SecurityConfig`, que consulta `UsuarioRepository` (sin clase service).
- `BCryptPasswordEncoder` para contraseñas.
- Login propio en `/login`; logout por POST con CSRF activo.

## 4. Validación

- Bean Validation en las entidades (`@NotBlank`, `@Pattern`, `@Email`, `@PastOrPresent`, `@DecimalMin/Max`).
- Reglas que requieren la base (duplicados, propietario existente, eliminación con dependencias) se validan en el controlador con `BindingResult.rejectValue`.

## 5. Configuración y despliegue

Variables de entorno: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `PORT`.
El Dockerfile y el despliegue en Render se hacen en el Sprint 3.

## 6. Decisiones (ADR)

Ver `docs/adr/`.
