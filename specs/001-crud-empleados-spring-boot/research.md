# Research: CRUD de Empleados

## Decision 1: Código HTTP para clave duplicada

- **Decision**: Responder `409 Conflict` cuando se intente crear un empleado con `clave` existente.
- **Rationale**: El conflicto es de estado del recurso (identificador ya ocupado), no de sintaxis del request.
- **Alternatives considered**:
  - `400 Bad Request`: demasiado genérico para conflicto de unicidad.
  - `422 Unprocessable Entity`: válido en algunos estilos, pero menos consistente para duplicidad de PK.

## Decision 2: Estrategia de autenticación

- **Decision**: Usar Spring Security con HTTP Basic para `/api/empleados/**`.
- **Rationale**: Es el requerimiento explícito del feature y suficiente para MVP backend.
- **Alternatives considered**:
  - JWT/OAuth2: mayor complejidad y fuera del alcance actual.

## Decision 3: Persistencia y migraciones

- **Decision**: PostgreSQL como base de datos y Flyway para versionado de esquema.
- **Rationale**: Alinea con la constitución y asegura reproducibilidad de estructura.
- **Alternatives considered**:
  - `ddl-auto` sin migraciones: menos trazabilidad y control de cambios.

## Decision 4: Contrato API y documentación

- **Decision**: Documentar API en OpenAPI 3 y exponer Swagger UI.
- **Rationale**: Facilita consumo, pruebas manuales y trazabilidad del contrato.
- **Alternatives considered**:
  - Documentación manual en README: no ejecutable ni validable automáticamente.

## Decision 5: Validación de campos

- **Decision**: Enforzar `@NotBlank` + `@Size(max=100)` en API y `VARCHAR(100)` en BD para `nombre`, `direccion`, `telefono`.
- **Rationale**: Garantiza consistencia entre capa de aplicación y persistencia.
- **Alternatives considered**:
  - Validación solo en BD: mensajes de error menos claros para cliente.
