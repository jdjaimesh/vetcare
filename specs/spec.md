# Especificación v1.0 — Sprint 1 (VetCare Santander)

> Fase 3 de SDD. Aprobación: **Puerta 1 (Product Owner)**.
> Alcance: acceso por rol, gestión de usuarios, propietarios y mascotas (HU-01, HU-02, HU-03).
> Notación EARS: *WHEN* (evento), *WHILE* (estado), *IF/THEN* (comportamiento no deseado), *WHERE* (característica opcional), *The system shall* (respuesta).

## 1. Contexto y actores

Clínica veterinaria que hoy maneja clientes, mascotas, citas e inventario en documentos separados. Sprint 1 crea la base: identidad, roles y los datos maestros (propietarios y mascotas).

| Rol | Alcance en el Sprint 1 |
|---|---|
| ADMIN | Gestiona usuarios; también puede administrar propietarios y mascotas |
| VETERINARIO | Consulta mascotas |
| RECEPCIONISTA | Administra propietarios y mascotas |
| AUXILIAR | Inicia sesión; su módulo (inventario) llega en el Sprint 2 |
| PROPIETARIO | Consulta únicamente sus propias mascotas |

## 2. Historias de usuario del sprint

| HU | Rol | Necesidad |
|---|---|---|
| HU-01 | Administrador | Crear usuarios y asignar uno de los 5 roles para controlar el acceso |
| HU-02 | Recepcionista | Registrar y actualizar propietarios para centralizar la información de clientes |
| HU-03 | Recepcionista | Registrar mascotas asociadas a un propietario válido para mantener su ficha |

## 3. Requisitos funcionales (EARS)

### HU-01 · Acceso y usuarios

- **REQ-VET-001** — *WHEN* un usuario envía credenciales válidas, *the system shall* autenticarlo y llevarlo a la página de inicio.
- **REQ-VET-002** — *The system shall* almacenar las contraseñas cifradas con BCrypt y nunca mostrarlas en pantalla.
- **REQ-VET-003** — *WHILE* un usuario está autenticado, *the system shall* mostrar solo las opciones de menú autorizadas para su rol.
- **REQ-VET-004** — *IF* un usuario solicita una ruta no autorizada para su rol, *THEN the system shall* rechazar el acceso (HTTP 403).
- **REQ-VET-005** — *IF* un usuario no autenticado solicita cualquier ruta distinta de `/login`, *THEN the system shall* redirigirlo al login.
- **REQ-VET-006** — *WHEN* el Administrador crea o edita un usuario, *the system shall* exigir nombre de usuario único (4 a 30 caracteres), nombre completo, rol y, al crear, contraseña de mínimo 6 caracteres.
- **REQ-VET-007** — *WHERE* el rol del usuario es PROPIETARIO, *the system shall* exigir su asociación con un propietario registrado.
- **REQ-VET-008** — *WHEN* el Administrador desactiva un usuario, *the system shall* impedirle iniciar sesión.
- **REQ-VET-009** — *IF* el Administrador intenta eliminar su propio usuario, *THEN the system shall* rechazar la operación.

### HU-02 · Propietarios

- **REQ-VET-010** — *WHEN* el Administrador o la Recepcionista guarda un propietario, *the system shall* exigir nombre, documento, teléfono (7 a 10 dígitos) y correo válido.
- **REQ-VET-011** — *IF* el documento ya existe en otro propietario, *THEN the system shall* rechazar el registro con un mensaje de error.
- **REQ-VET-012** — *WHEN* el usuario escribe en el buscador de propietarios, *the system shall* listar los que coincidan por nombre o documento.
- **REQ-VET-013** — *IF* el propietario tiene mascotas o un usuario de acceso asociado, *THEN the system shall* impedir su eliminación.

### HU-03 · Mascotas

- **REQ-VET-014** — *WHEN* la Recepcionista o el Administrador guarda una mascota, *the system shall* exigir propietario existente, nombre, especie, raza, sexo, fecha de nacimiento y peso.
- **REQ-VET-015** — *IF* la fecha de nacimiento es futura o el peso no está entre 0,1 y 150 kg, *THEN the system shall* rechazar el registro.
- **REQ-VET-016** — *WHILE* el usuario autenticado tiene rol PROPIETARIO, *the system shall* listar únicamente las mascotas de su propietario asociado.
- **REQ-VET-017** — *The system shall* calcular y mostrar la edad de cada mascota a partir de su fecha de nacimiento.

## 4. Requisitos no funcionales

- **RNF-01** Contraseñas con BCrypt. *(REQ-VET-002)*
- **RNF-02** Interfaz responsive con Bootstrap 5.
- **RNF-03** Listados con respuesta menor a 3 segundos con 15 registros de prueba.
- **RNF-04** Credenciales por variables de entorno; sin secretos en el repositorio.
- **RNF-05** Arquitectura MVC sin capa `service`.
- **RNF-06** Cumplimiento de la Ley 1581 de 2012: datos ficticios en pruebas y acceso a datos personales solo por rol.

## 5. Reglas de negocio

- **RN-01** Una mascota pertenece a un solo propietario; un propietario puede tener varias mascotas.
- **RN-02** El propietario solo ve sus propias mascotas.
- **RN-03** Un propietario con mascotas no puede eliminarse.
- **RN-04** El documento del propietario y el usuario de acceso son únicos.

## 6. Restricciones

Java 17+, Spring Boot 3, Spring Data JPA, Spring Security, Thymeleaf, MySQL online, **sin carpeta `service`**, despliegue en Render (Sprint 3).

## 7. Matriz de permisos

| Módulo | ADMIN | RECEPCIONISTA | VETERINARIO | AUXILIAR | PROPIETARIO |
|---|---|---|---|---|---|
| Usuarios | CRUD | — | — | — | — |
| Propietarios | CRUD | CRUD | — | — | — |
| Mascotas | CRUD | CRUD | Ver | — | Ver las suyas |

## 8. Criterios de aceptación (Given/When/Then)

**AC-001** (REQ-VET-001)
- *Given* el usuario `recepcion` activo, *When* ingresa con su contraseña correcta, *Then* ve la página de inicio.
- *Given* credenciales incorrectas, *When* intenta ingresar, *Then* permanece en el login con el mensaje "Usuario o contraseña incorrectos".

**AC-002** (REQ-VET-002)
- *Given* un usuario recién creado, *When* se consulta la tabla `usuarios`, *Then* la columna `password` contiene un hash que empieza por `$2a$` o `$2b$`.
- *Given* el formulario de edición de un usuario, *When* se abre, *Then* el campo de contraseña está vacío.

**AC-003** (REQ-VET-003)
- *Given* un usuario con rol PROPIETARIO, *When* abre la página de inicio, *Then* el menú solo muestra "Mascotas".
- *Given* un usuario con rol ADMIN, *When* abre la página de inicio, *Then* el menú muestra Propietarios, Mascotas y Usuarios.

**AC-004** (REQ-VET-004)
- *Given* el rol RECEPCIONISTA, *When* escribe la URL `/usuarios`, *Then* recibe un error 403.
- *Given* el rol PROPIETARIO, *When* escribe la URL `/propietarios`, *Then* recibe un error 403.
- *Given* el rol VETERINARIO, *When* escribe la URL `/mascotas/nueva`, *Then* recibe un error 403.

**AC-005** (REQ-VET-005)
- *Given* una sesión cerrada, *When* se abre `/mascotas`, *Then* se redirige a `/login`.

**AC-006** (REQ-VET-006)
- *Given* el usuario `admin`, *When* intenta crear otro usuario con el nombre `admin`, *Then* aparece "Ese nombre de usuario ya existe" y no se guarda.
- *Given* el formulario de nuevo usuario, *When* se deja la contraseña vacía, *Then* aparece "La contraseña es obligatoria".
- *Given* la edición de un usuario, *When* se deja la contraseña vacía, *Then* se conserva la contraseña anterior.

**AC-007** (REQ-VET-007)
- *Given* el rol PROPIETARIO seleccionado, *When* no se elige propietario asociado, *Then* aparece "Selecciona el propietario asociado a este usuario".

**AC-008** (REQ-VET-008)
- *Given* un usuario desactivado, *When* intenta ingresar con su contraseña correcta, *Then* no accede al sistema.

**AC-009** (REQ-VET-009)
- *Given* el admin autenticado, *When* intenta eliminar su propio usuario, *Then* aparece "No puedes eliminar tu propio usuario" y el usuario sigue existiendo.

**AC-010** (REQ-VET-010)
- *Given* el formulario de propietario, *When* el teléfono tiene letras o menos de 7 dígitos, *Then* aparece "El teléfono debe tener entre 7 y 10 dígitos".
- *Given* el formulario de propietario, *When* el correo no tiene formato válido, *Then* aparece "Correo no válido".

**AC-011** (REQ-VET-011)
- *Given* el propietario con documento 1098765401, *When* se registra otro con ese mismo documento, *Then* aparece "Ya existe un propietario con ese documento".

**AC-012** (REQ-VET-012)
- *Given* los propietarios de prueba, *When* se busca "gómez", *Then* solo aparece Andrea Gómez.

**AC-013** (REQ-VET-013)
- *Given* Laura Martínez con mascotas y usuario asociado, *When* se intenta eliminarla, *Then* aparece un mensaje de error y sigue existiendo.
- *Given* un propietario sin mascotas ni usuario, *When* se elimina, *Then* desaparece de la lista.

**AC-014** (REQ-VET-014)
- *Given* el formulario de mascota, *When* no se selecciona propietario, *Then* aparece "Selecciona el propietario".
- *Given* todos los datos completos, *When* se guarda, *Then* la mascota aparece en el listado con su propietario.

**AC-015** (REQ-VET-015)
- *Given* el formulario de mascota, *When* la fecha de nacimiento es de mañana, *Then* aparece "La fecha de nacimiento no puede ser futura".
- *Given* el formulario de mascota, *When* el peso es 0 o 200, *Then* aparece el mensaje de rango de peso.

**AC-016** (REQ-VET-016)
- *Given* el usuario `laura` (rol PROPIETARIO), *When* abre Mascotas, *Then* solo ve a Max y Luna.

**AC-017** (REQ-VET-017)
- *Given* una mascota nacida hace 2 años y 3 meses, *When* se lista, *Then* la edad muestra "2 años".

## 9. Trazabilidad (Sprint 1)

| Requisito | Criterio | Implementación | Tarea |
|---|---|---|---|
| REQ-VET-001, 005 | AC-001, AC-005 | `SecurityConfig`, `login.html` | T-04 |
| REQ-VET-002 | AC-002 | `SecurityConfig` (BCrypt), `UsuarioController` | T-04, T-05 |
| REQ-VET-003, 004 | AC-003, AC-004 | `SecurityConfig`, `fragments/layout.html` | T-04 |
| REQ-VET-006 a 009 | AC-006 a AC-009 | `UsuarioController`, `Usuario` | T-05 |
| REQ-VET-010 a 013 | AC-010 a AC-013 | `PropietarioController`, `Propietario`, `PropietarioRepository` | T-06 |
| REQ-VET-014 a 017 | AC-014 a AC-017 | `MascotaController`, `Mascota`, `MascotaRepository` | T-07 |
| Datos de prueba | AC-012, AC-016 | `DatosIniciales` | T-08 |

La columna de evidencia (capturas o resultado de prueba) se completa en el Sprint Review.
