# Feature Specification: CRUD de Departamentos y Relacion con Empleados

**Feature Branch**: `003-crud-departamentos-spring-boot`  
**Created**: 2026-03-19  
**Status**: Draft  
**Input**: User description: "Implementar el CRUD de departamentos y relacionarlo con empleados sobre el proyecto actual, manteniendo JWT, Swagger, PostgreSQL y Docker, sin regenerar el proyecto."

## Clarifications

### Session 2026-03-19

- Esta iteracion se implementa de forma incremental sobre `DSW02-Practica01`.
- Se agrega entidad Departamento con atributos base obligatorios: `clave` y `nombre`.
- Regla de cardinalidad confirmada: un empleado pertenece a un departamento; un departamento puede tener varios empleados.
- Obligatoriedad confirmada: al finalizar la iteracion 003, todo empleado MUST tener departamento asignado (no nullable en estado final).
- Estrategia de transicion confirmada: durante migracion se MAY usar una etapa transitoria controlada para poblar datos, pero el estado final MUST quedar no-null.
- Regla de unicidad confirmada: el nombre de departamento debe ser unico (case-insensitive) en el dominio activo.
- Regla de eliminacion confirmada: no se permite eliminar un departamento con empleados asociados; debe responder conflicto de negocio.
- Seguridad de departamentos confirmada: los endpoints `/api/v1/departamentos/**` MUST estar protegidos con JWT.
- Representacion JSON confirmada: las respuestas MUST usar DTOs sin referencias bidireccionales directas para evitar ciclos de serializacion.
- Estrategia de representacion confirmada:
	- `EmpleadoResponse` incluye referencia plana de departamento (`departamentoClave`, `departamentoNombre`) cuando aplique.
	- `DepartamentoResponse` NO incluye grafo completo de empleados por defecto; si expone empleados, debe ser en forma resumida y acotada.
- Formato de clave de departamento confirmado: `DEP-<numero>` como identificador logico estable y versionado en API.
- Contrato API: se mantiene versionado y documentacion en Swagger/OpenAPI.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Alta y consulta de departamentos (Priority: P1)

Como usuario autenticado, quiero crear y consultar departamentos para organizar la informacion de empleados por area.

**Why this priority**: Sin alta y consulta de departamentos no existe base funcional para relacionar empleados con estructura organizacional.

**Independent Test**: Se prueba crear un departamento con nombre unico, obtenerlo por clave y listar departamentos con paginacion en rutas versionadas.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado con permisos de negocio, **When** envia `POST /api/v1/departamentos` con datos validos, **Then** el sistema crea el departamento y responde `201` con la representacion persistida.
2. **Given** un departamento existente, **When** se llama `GET /api/v1/departamentos/{clave}`, **Then** el sistema responde `200` con sus datos.
3. **Given** departamentos registrados, **When** se llama `GET /api/v1/departamentos?page=0&size=10`, **Then** el sistema responde `200` con resultados paginados.
4. **Given** un nombre de departamento ya registrado (ignorando mayusculas/minusculas), **When** se intenta crear otro con el mismo nombre logico, **Then** el sistema responde `409 Conflict`.

---

### User Story 2 - Actualizacion, eliminacion y reglas de integridad (Priority: P1)

Como usuario autenticado, quiero actualizar y eliminar departamentos de manera segura para mantener consistencia del modelo con empleados.

**Why this priority**: El CRUD solo es completo cuando se pueden modificar y remover departamentos bajo reglas de integridad referencial.

**Independent Test**: Se prueba actualizar un departamento, eliminar uno sin empleados asociados, y rechazar eliminacion de uno con empleados asociados.

**Acceptance Scenarios**:

1. **Given** un departamento existente, **When** se llama `PUT /api/v1/departamentos/{clave}` con datos validos, **Then** el sistema responde `200` con el departamento actualizado.
2. **Given** un departamento sin empleados asociados, **When** se llama `DELETE /api/v1/departamentos/{clave}`, **Then** el sistema responde `204` y elimina el registro.
3. **Given** un departamento con uno o mas empleados asociados, **When** se llama `DELETE /api/v1/departamentos/{clave}`, **Then** el sistema responde `409 Conflict` y no elimina el registro.
4. **Given** una clave inexistente con formato valido, **When** se invoca `PUT` o `DELETE`, **Then** el sistema responde `404 Not Found`.

---

### User Story 3 - Relacion Empleado-Departamento y compatibilidad tecnica (Priority: P2)

Como desarrollador/consumidor de API, quiero que empleados y departamentos queden relacionados sin romper seguridad, despliegue local ni documentacion.

**Why this priority**: La iteracion 003 debe ampliar el dominio sin regresion de las capacidades existentes.

**Independent Test**: Se valida que el modelo persiste la relacion, que la API sigue protegida con reglas vigentes y que la app arranca en Docker/PostgreSQL con Swagger actualizado.

**Acceptance Scenarios**:

1. **Given** un empleado existente y un departamento existente, **When** se asocia el empleado al departamento segun reglas del modelo, **Then** la persistencia mantiene integridad referencial y la consulta refleja la relacion.
2. **Given** la aplicacion levantada con PostgreSQL en Docker, **When** se ejecutan migraciones de iteracion 003, **Then** el arranque completa sin errores y el esquema queda consistente.
3. **Given** Swagger/OpenAPI habilitado, **When** se consulta la documentacion, **Then** aparecen endpoints de departamentos y sus respuestas de error/negocio.
4. **Given** rutas protegidas de negocio, **When** se consumen sin token valido, **Then** se mantiene el comportamiento de autenticacion definido por iteraciones previas.
5. **Given** respuestas de empleados y departamentos, **When** se serializan a JSON, **Then** no se generan ciclos de serializacion ni payloads recursivos.

---

### Edge Cases

- ¿Que ocurre si se intenta crear/actualizar un departamento con `nombre` vacio o nulo? Debe responder `400 Bad Request`.
- ¿Que ocurre si se intenta crear un departamento con nombre duplicado en diferente combinacion de mayusculas/minusculas? Debe responder `409 Conflict`.
- ¿Que ocurre si se intenta consultar/actualizar/eliminar una `clave` de departamento inexistente? Debe responder `404 Not Found`.
- ¿Que ocurre si se intenta eliminar un departamento con empleados asociados? Debe responder `409 Conflict` y conservar datos.
- ¿Que ocurre si la migracion de relacion empleado-departamento encuentra empleados sin departamento asignado? Debe resolverse por estrategia de migracion definida (valor por defecto temporal o politica de transicion documentada) sin ruptura de arranque.
- ¿Que ocurre si PostgreSQL no esta disponible al iniciar? La aplicacion debe fallar explicitamente y no operar en estado inconsistente.
- ¿Que ocurre si se intenta persistir o actualizar un empleado sin departamento en estado final de iteracion 003? Debe responder error de validacion/negocio y no persistir inconsistencia.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST introducir la entidad `Departamento` con atributos base `clave` y `nombre`.
- **FR-002**: El sistema MUST exponer endpoint versionado `POST /api/v1/departamentos` para crear departamentos.
- **FR-003**: El sistema MUST exponer endpoint versionado `GET /api/v1/departamentos` con paginacion (`page`, `size`).
- **FR-004**: El sistema MUST exponer endpoint versionado `GET /api/v1/departamentos/{clave}` para consulta por clave.
- **FR-005**: El sistema MUST exponer endpoint versionado `PUT /api/v1/departamentos/{clave}` para actualizacion.
- **FR-006**: El sistema MUST exponer endpoint versionado `DELETE /api/v1/departamentos/{clave}` para eliminacion.
- **FR-007**: El nombre del departamento MUST ser unico en el dominio de departamentos (comparacion case-insensitive).
- **FR-008**: El sistema MUST modelar la relacion donde un empleado pertenece a un departamento y un departamento agrupa multiples empleados.
- **FR-009**: La persistencia MUST mantener integridad referencial entre empleados y departamentos.
- **FR-010**: En estado final de iteracion 003, la referencia de departamento en empleado MUST ser obligatoria (no nullable).
- **FR-011**: El sistema MUST rechazar eliminacion de departamentos con empleados asociados con `409 Conflict`.
- **FR-012**: Los endpoints `/api/v1/departamentos/**` MUST requerir autenticacion JWT.
- **FR-013**: El sistema MUST mantener compatibilidad con el esquema de seguridad vigente (JWT y reglas administrativas actuales).
- **FR-014**: El sistema MUST mantener compatibilidad con PostgreSQL y flujo Docker existente.
- **FR-015**: El sistema MUST documentar en Swagger/OpenAPI los endpoints de departamentos y sus respuestas esperadas.
- **FR-016**: Todos los cambios de esta iteracion MUST aplicarse sobre la estructura actual del proyecto sin regeneracion desde cero.
- **FR-017**: La iteracion 003 MUST preservar el comportamiento funcional de iteracion 002 salvo ajustes estrictamente necesarios para la relacion Empleado-Departamento.
- **FR-018**: Los endpoints GET de listado de departamentos MUST aplicar paginacion por defecto `page=0` y `size=10`, y MUST rechazar `size > 100`.
- **FR-019**: Las respuestas JSON MUST usar DTOs que eviten ciclos de serializacion entre Empleado y Departamento.
- **FR-020**: La entidad Departamento MUST exponer clave logica con formato `DEP-<numero>` en respuestas API.
- **FR-021**: Si se incluye informacion de empleados dentro de `DepartamentoResponse`, MUST ser una vista resumida y acotada, no el objeto completo recursivo.

### Key Entities *(include if feature involves data)*

- **Departamento**: Entidad organizacional con `clave` identificadora y `nombre` unico.
- **Empleado** (extension de dominio): Mantiene su modelo existente e incorpora referencia a `Departamento` para representar pertenencia.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones CRUD de departamentos (`crear`, `listar`, `obtener`, `actualizar`, `eliminar`) responde con codigos HTTP esperados en pruebas de API autenticada.
- **SC-002**: El 100% de intentos de crear/actualizar departamentos con nombre duplicado (case-insensitive) responde `409 Conflict`.
- **SC-003**: El 100% de intentos de eliminar departamentos con empleados asociados responde `409 Conflict` y no elimina registros.
- **SC-004**: El 100% de operaciones sobre claves inexistentes de departamentos responde `404 Not Found`.
- **SC-005**: La aplicacion arranca correctamente con PostgreSQL en Docker aplicando migraciones de iteracion 003 sin errores de integridad.
- **SC-006**: Swagger/OpenAPI publica endpoints de departamentos con contratos versionados y errores de negocio documentados.
- **SC-007**: No se requiere regenerar proyecto ni alterar arquitectura base para completar la iteracion 003.
- **SC-008**: Las rutas y flujos de seguridad existentes (JWT y reglas administrativas) mantienen comportamiento esperado tras la iteracion 003.
