# VetCare Santander — Sprint 1

**Meta del sprint:** cada uno de los 5 roles inicia sesión y ve solo su menú, y recepción registra propietarios y mascotas en MySQL online.

**Historias:** HU-01 (usuarios y roles), HU-02 (propietarios), HU-03 (mascotas).

## Documentos SDD (carpeta `specs/` y `docs/adr/`)

| Archivo | Contenido |
|---|---|
| `specs/constitution.md` | Principios no negociables, reglas para la IA y Definition of Done |
| `specs/spec.md` | Requisitos EARS `REQ-VET-###`, criterios Given/When/Then `AC-###` y trazabilidad |
| `specs/plan.md` | Arquitectura MVC sin capa service, modelo de datos y seguridad |
| `specs/tasks.md` | Tareas T-00 a T-10 con requisito, criterio, quién hace y quién valida |
| `docs/adr/` | Decisiones de arquitectura (ADR-001 a ADR-004) |

## Cómo ejecutarlo

1. Crea una base MySQL online (por ejemplo Aiven, Railway o similar).
2. Define estas variables de entorno (en Spring Tools: *Run Configurations → Environment*):
   - `SPRING_DATASOURCE_URL` → `jdbc:mysql://HOST:PUERTO/BASE?useSSL=true&serverTimezone=America/Bogota`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
3. Ejecuta como *Spring Boot App* y abre http://localhost:8080

Sin variables, se conecta a un MySQL local (`localhost:3306`, usuario `root`, base `vetcare`).
Al primer arranque se crean las tablas y los datos de prueba.

## Usuarios de prueba (contraseña de todos: `Vet2026*`)

| Usuario | Rol |
|---|---|
| admin | Administrador |
| veterinario | Veterinario |
| recepcion | Recepcionista |
| auxiliar | Auxiliar de inventario |
| laura | Propietario (ve solo a Max y Luna) |

## Matriz de permisos

| Módulo | ADMIN | RECEPCIONISTA | VETERINARIO | AUXILIAR | PROPIETARIO |
|---|---|---|---|---|---|
| Usuarios | CRUD | — | — | — | — |
| Propietarios | CRUD | CRUD | — | — | — |
| Mascotas | CRUD | CRUD | Ver | — | Ver las suyas |

## Estructura (sin carpeta service)

```
vetcare
├── specs/ · docs/adr/
└── src/main/java/com/uts/vetcare
    ├── config/       SecurityConfig, DatosIniciales
    ├── controller/   Home, Usuario, Propietario, Mascota
    ├── model/        Rol, Usuario, Propietario, Mascota
    └── repository/   UsuarioRepository, PropietarioRepository, MascotaRepository
```

## Pendiente para cerrar el sprint

- Ejecutar los criterios `AC-001` a `AC-017` con cada rol y guardar las capturas (tarea T-09).
- Sprint Review y Retrospectiva (tarea T-10).
