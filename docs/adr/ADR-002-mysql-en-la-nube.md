# ADR-002 — MySQL administrado en la nube

- **Estado:** Aceptada
- **Contexto:** La base debe estar online y accesible desde Render.
- **Decisión:** MySQL con `mysql-connector-j`; credenciales por variables de entorno; esquema generado con `ddl-auto=update`.
- **Consecuencias:** Los planes gratuitos pueden tener límites de conexiones o caducidad; verificar el estado de la base antes de cada Sprint Review. El esquema se ajusta automáticamente, pero no debe depender de `ddl-auto=update` en producción real.
