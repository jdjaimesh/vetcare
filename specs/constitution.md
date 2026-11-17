# Constitución del proyecto — VetCare Santander

> Fase 0 de SDD. Aprobación: **Puerta 0 (Equipo)**. Estos principios no se negocian durante los sprints.

## 1. Principios no negociables

1. **La especificación es la fuente de verdad.** Ningún código se escribe sin un requisito (`REQ-VET-###`) y al menos un criterio de aceptación (`AC-###`) aprobados.
2. **Sin capa `service`.** Los `@Controller` hablan directamente con los repositorios JPA y las entidades. La lógica de negocio vive en controladores, consultas `@Query` y métodos de las entidades. No existe el paquete `service`.
3. **Persistencia solo con JPA/Hibernate** sobre MySQL en la nube. No se usa JDBC manual ni SQL embebido fuera de `@Query`.
4. **Vistas solo con Thymeleaf + Bootstrap 5.**
5. **Seguridad por defecto.** Toda ruta exige autenticación salvo `/login`. Las contraseñas se guardan con BCrypt. Cada ruta declara los roles autorizados.
6. **Cuatro ojos.** Quien implementa una tarea no la revisa: otra persona valida el diff y los criterios de aceptación.
7. **Sin secretos en el repositorio.** Credenciales solo por variables de entorno (`SPRING_DATASOURCE_*`).
8. **Datos personales.** Se cumple la Ley 1581 de 2012: solo se piden datos necesarios y se muestran únicamente al rol autorizado. Los datos de prueba son ficticios.

## 2. Reglas para el uso de IA

- La IA genera código solo a partir de `spec.md`, `plan.md` y `tasks.md` vigentes.
- Todo cambio generado por IA pasa por revisión humana antes de integrarse a `main`.
- Los cambios de alcance se registran primero en un `delta-spec.md` y los aprueba el Product Owner.
- Se mantiene la bitácora de uso responsable de IA (qué se pidió, qué se aceptó, qué se corrigió).

## 3. Definition of Done (DoD)

- [ ] El código cumple el requisito y todos sus criterios `AC-###`.
- [ ] Validaciones con mensajes claros en la vista.
- [ ] Probado con cada rol que interviene (acceso permitido y acceso rechazado).
- [ ] Revisado por una persona distinta a quien lo implementó.
- [ ] Sin credenciales ni datos reales en el repositorio.
- [ ] Funciona contra la base MySQL online.
- [ ] Trazabilidad actualizada (requisito → criterio → tarea → evidencia).
- [ ] Tarea movida a **Hecho** en el tablero.

## 4. Tablero Kanban

Columnas: **Por hacer · En curso (WIP 3) · En revisión (WIP 2) · Bloqueado · Hecho.**
Cada tarjeta referencia su `REQ-VET-###` y su `AC-###`.
