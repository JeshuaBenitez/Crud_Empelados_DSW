# Tasks: CRUD de Departamentos y Relacion con Empleados

**Input**: Design documents from `/specs/003-crud-departamentos-spring-boot/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `contracts/` (a crear/actualizar), `quickstart.md` (a actualizar)

**Tests**: Se incluyen tareas de pruebas unitarias, integracion, seguridad, migraciones y regresion por tratarse de una expansion de dominio con integridad referencial.

**Organization**: Tareas agrupadas por fases e historias para ejecucion incremental segura sobre el proyecto existente (sin regeneracion).

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos y sin dependencia directa)
- **[Story]**: `US1`, `US2`, `US3`
- Cada tarea incluye rutas concretas de archivo

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar base de trabajo para iteracion 003 sin tocar arquitectura.

- [ ] T001 Verificar dependencias vigentes para JPA/Flyway/Security/OpenAPI en `DSW02-Practica01/pom.xml`
- [ ] T002 [P] Confirmar propiedades de entorno para PostgreSQL/Flyway/JWT en `DSW02-Practica01/src/main/resources/application.properties`
- [ ] T003 [P] Verificar compatibilidad de stack Docker (api + postgres) sin cambios disruptivos en `DSW02-Practica01/docker-compose.yml`
- [ ] T004 [P] Preparar carpeta de contrato/documentacion de 003 en `specs/003-crud-departamentos-spring-boot/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Migraciones y modelo base de Departamentos + relacion con Empleados.

**⚠️ CRITICAL**: Ninguna historia inicia antes de cerrar esta fase.

### 2.1 Migraciones Flyway (orden seguro)

- [ ] T005 Crear migracion de tabla `departamentos` con atributos base y soporte de clave logica `DEP-<numero>` en `DSW02-Practica01/src/main/resources/db/migration/V3__create_departamentos_table.sql`
- [ ] T006 Crear migracion para agregar FK transitoria nullable en `empleados` hacia `departamentos` en `DSW02-Practica01/src/main/resources/db/migration/V3_1__add_departamento_fk_to_empleados.sql`
- [ ] T007 Crear migracion de backfill para empleados existentes (asignacion de departamento por politica definida) en `DSW02-Practica01/src/main/resources/db/migration/V3_2__backfill_empleados_departamento.sql`
- [ ] T008 Crear migracion para aplicar constraint final no-null de departamento en empleados en `DSW02-Practica01/src/main/resources/db/migration/V3_3__enforce_empleado_departamento_not_null.sql`
- [ ] T009 Crear migracion de unicidad case-insensitive de `departamentos.nombre` en `DSW02-Practica01/src/main/resources/db/migration/V3_4__add_departamento_nombre_unique_ci.sql`
- [ ] T010 Verificar orden de migraciones y compatibilidad con historico 001/002 (sin modificar migraciones previas) en `DSW02-Practica01/src/main/resources/db/migration/`

### 2.2 Modelo y persistencia base

- [ ] T011 [P] Crear entidad `Departamento` (incluyendo clave logica `DEP-<numero>`) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/Departamento.java`
- [ ] T012 [P] Actualizar entidad `Empleado` para relacion N:1 obligatoria con `Departamento` en estado final en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/Empleado.java`
- [ ] T013 [P] Crear `DepartamentoRepository` con busquedas por clave/nombre case-insensitive en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/repository/DepartamentoRepository.java`
- [ ] T014 [P] Extender `EmpleadoRepository` con consulta de existencia por departamento para regla de borrado en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/repository/EmpleadoRepository.java`

**Checkpoint**: Esquema y modelo listos para implementar CRUD.

---

## Phase 3: User Story 1 - CRUD de Departamentos (Priority: P1) 🎯 MVP

**Goal**: Exponer CRUD completo de departamentos con reglas de negocio principales.

**Independent Test**: `POST/GET/GET by clave/PUT/DELETE` en `/api/v1/departamentos` con JWT y codigos esperados.

### Tests for User Story 1

- [ ] T015 [P] [US1] Crear tests unitarios de `DepartamentoService` para crear/consultar/actualizar/eliminar en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/service/DepartamentoServiceUs1Test.java`
- [ ] T016 [P] [US1] Crear test de integracion de `POST /api/v1/departamentos` (`201`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoCreateIntegrationTest.java`
- [ ] T017 [P] [US1] Crear tests de integracion de `GET` listado y `GET by clave` en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoReadIntegrationTest.java`
- [ ] T018 [P] [US1] Crear test de integracion de `PUT /api/v1/departamentos/{clave}` (`200`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoUpdateIntegrationTest.java`
- [ ] T019 [P] [US1] Crear test de integracion de `DELETE /api/v1/departamentos/{clave}` (`204` cuando no hay asociados) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoDeleteIntegrationTest.java`

### Implementation for User Story 1

- [ ] T020 [US1] Implementar `DepartamentoService` con operaciones CRUD y validaciones base en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/DepartamentoService.java`
- [ ] T021 [US1] Implementar controlador versionado `/api/v1/departamentos` en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/DepartamentoController.java`
- [ ] T022 [US1] Crear DTOs de request/response para departamentos en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/dto/`
- [ ] T023 [US1] Integrar respuestas `404` para clave inexistente en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`

**Checkpoint**: CRUD de departamentos funcional en condiciones nominales.

---

## Phase 4: User Story 2 - Reglas de negocio e integridad (Priority: P1)

**Goal**: Endurecer reglas de unicidad, borrado restringido e integridad referencial.

**Independent Test**: Conflictos `409` por nombre duplicado y por eliminar departamento con empleados asociados.

### Tests for User Story 2

- [ ] T024 [P] [US2] Crear test unitario de unicidad case-insensitive de nombre en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/service/DepartamentoNombreUnicoTest.java`
- [ ] T025 [P] [US2] Crear test de integracion para `409` por nombre duplicado en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoConflictIntegrationTest.java`
- [ ] T026 [P] [US2] Crear test de integracion para `409` al eliminar departamento con empleados asociados en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/DepartamentoDeleteConflictIntegrationTest.java`
- [ ] T027 [P] [US2] Crear test de integracion para constraint final no-null en relacion empleado-departamento en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/EmpleadoDepartamentoNotNullIntegrationTest.java`

### Implementation for User Story 2

- [ ] T028 [US2] Implementar validacion de nombre unico (case-insensitive) y mapear conflicto a `409` en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/DepartamentoService.java`
- [ ] T029 [US2] Implementar regla de borrado restringido (409 con asociados) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/DepartamentoService.java`
- [ ] T030 [US2] Ajustar manejo de excepciones de conflicto y validacion en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`

**Checkpoint**: Reglas de negocio cerradas y consistentes con spec.

---

## Phase 5: User Story 3 - Relacion en API de empleados + DTO/Serializacion (Priority: P2)

**Goal**: Exponer relacion empleado-departamento sin ciclos JSON ni regresiones en empleados.

**Independent Test**: Respuestas de empleados incluyen referencia plana de departamento sin recursion.

### Tests for User Story 3

- [ ] T031 [P] [US3] Crear test de integracion para respuesta de empleado con `departamentoClave` y `departamentoNombre` en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoDepartamentoResponseIntegrationTest.java`
- [ ] T032 [P] [US3] Crear test de no-regresion de endpoints de empleados existentes en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoRegressionIntegrationTest.java`
- [ ] T033 [P] [US3] Crear test de serializacion para ausencia de ciclos en DTOs en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/SerializationNoCyclesIntegrationTest.java`

### Implementation for User Story 3

- [ ] T034 [US3] Ajustar DTOs de empleados para referencia plana al departamento en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/dto/EmpleadoResponse.java`
- [ ] T035 [US3] Ajustar mapeos servicio/controlador de empleados para poblar referencia de departamento en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java` y `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java`
- [ ] T036 [US3] Verificar que `DepartamentoResponse` no exponga grafo recursivo completo en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/dto/`

**Checkpoint**: Relacion expuesta de forma segura y serializable.

---

## Phase 6: Security (JWT for Departamentos)

**Purpose**: Proteger `/api/v1/departamentos/**` con JWT manteniendo compatibilidad de seguridad existente.

- [ ] T037 [P] Ajustar `SecurityConfig` para incluir endpoints de departamentos en cadena JWT de negocio en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java`
- [ ] T038 [P] Crear test de seguridad sin token (`401`) para departamentos en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/DepartamentoJwtMissingTokenTest.java`
- [ ] T039 [P] Crear test de seguridad con token invalido (`401`) para departamentos en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/DepartamentoJwtInvalidTokenTest.java`
- [ ] T040 [P] Crear test de seguridad con token valido (`2xx` segun endpoint) para departamentos en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/DepartamentoJwtValidTokenTest.java`

---

## Phase 7: OpenAPI/Swagger

**Purpose**: Documentar contrato de departamentos, seguridad Bearer y errores de negocio.

- [ ] T041 [P] Actualizar configuracion OpenAPI para incluir endpoints de departamentos y esquema JWT en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/OpenApiConfig.java`
- [ ] T042 [P] Crear/actualizar contrato OpenAPI de iteracion 003 en `specs/003-crud-departamentos-spring-boot/contracts/departamentos-openapi.yaml`
- [ ] T043 [P] Documentar errores `409` (duplicado de nombre y borrado con asociados) en contrato OpenAPI `specs/003-crud-departamentos-spring-boot/contracts/departamentos-openapi.yaml`
- [ ] T044 Actualizar quickstart de 003 con flujo login + CRUD departamentos en `specs/003-crud-departamentos-spring-boot/quickstart.md`

---

## Phase 8: Compatibility & Validation (Docker/PostgreSQL + Smoke)

**Purpose**: Validar compatibilidad tecnica final de la iteracion.

- [ ] T045 [P] Ejecutar validacion de migraciones 003 sobre base existente en entorno Docker/PostgreSQL (documentar evidencia) en `specs/003-crud-departamentos-spring-boot/quickstart.md`
- [ ] T046 [P] Ejecutar suite de pruebas completa con `mvn test` en `DSW02-Practica01/`
- [ ] T047 Ejecutar compilacion final con `mvn -DskipTests compile` en `DSW02-Practica01/`
- [ ] T048 [P] Ejecutar smoke test final (login JWT + CRUD departamentos + verificacion de 409/404) y registrar evidencia en `specs/003-crud-departamentos-spring-boot/quickstart.md`
- [ ] T049 Validar no-regresion de comportamiento de iteracion 002 y registrar resultado en `specs/003-crud-departamentos-spring-boot/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea historias.
- **Phase 3-5 (User Stories)**: dependen de Foundational.
- **Phase 6 (Security)**: puede correr tras Phase 3 (controller endpoints ya presentes) y en paralelo con Phase 7.
- **Phase 7 (OpenAPI/Swagger)**: depende de endpoints definidos (Phase 3/4/5).
- **Phase 8 (Compatibility & Validation)**: depende de cierre de fases funcionales.

### Story Dependencies

- **US1 (P1)**: habilita CRUD base de departamentos.
- **US2 (P1)**: endurece reglas de negocio sobre US1.
- **US3 (P2)**: integra relacion en respuestas de empleados y evita ciclos.

### Migracion Segura (obligatoria)

1. Crear tabla departamentos y soporte de clave (`T005`).
2. Agregar FK nullable (`T006`).
3. Ejecutar backfill (`T007`).
4. Aplicar no-null final (`T008`).
5. Aplicar unicidad case-insensitive (`T009`).

### Parallel Opportunities

- `T002`, `T003`, `T004` en paralelo.
- `T011`, `T012`, `T013`, `T014` en paralelo tras `T005..T010`.
- Tests de cada historia en paralelo por archivos distintos.
- `T037..T040` en paralelo parcial con `T041..T044` una vez definidos endpoints.
- `T045`, `T046`, `T048` en paralelo controlado en fase final.

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup + Foundational.
2. Completar CRUD nominal de departamentos (US1).
3. Validar endpoints base y paginacion.

### Incremental Delivery

1. Entrega 1: Migraciones + modelo + CRUD base departamentos.
2. Entrega 2: Reglas de negocio de conflicto (`409`) e integridad final.
3. Entrega 3: Ajustes en empleados + DTOs sin ciclos + seguridad JWT completa.
4. Entrega 4: OpenAPI/quickstart + validacion dockerizada + smoke/regresion.

### Safety Gates

- No avanzar a constraint final no-null sin backfill validado.
- No habilitar cierre de iteracion sin pruebas de seguridad y regresion en verde.
- No modificar migraciones historicas de 001/002; solo agregar nuevas.

---

## Criterios de Aceptacion Operativos (derivados de analyze)

### Gate A - Migracion segura antes de no-null

- [ ] CA-001 El backfill de empleados a departamento finaliza con 0 registros sin departamento antes de aplicar no-null.
- [ ] CA-002 Existe evidencia documentada del conteo pre y post backfill en `specs/003-crud-departamentos-spring-boot/quickstart.md`.
- [ ] CA-003 No se ejecuta T008 hasta cumplir CA-001 y CA-002.

### Gate B - Seguridad JWT en departamentos

- [ ] CA-004 Sin token en `/api/v1/departamentos/**` retorna 401.
- [ ] CA-005 Con token invalido en `/api/v1/departamentos/**` retorna 401.
- [ ] CA-006 Con token valido en `/api/v1/departamentos/**` retorna codigo exitoso segun endpoint.
- [ ] CA-007 No existe bypass por Basic en rutas de negocio de departamentos.

### Gate C - Integridad de negocio y contrato HTTP

- [ ] CA-008 Crear o actualizar departamento con nombre duplicado (case-insensitive) retorna 409 de forma consistente.
- [ ] CA-009 Eliminar departamento con empleados asociados retorna 409 y no elimina datos.
- [ ] CA-010 Operaciones con clave inexistente de departamento retornan 404.

### Gate D - Serializacion y DTOs sin ciclos

- [ ] CA-011 `EmpleadoResponse` incluye referencia plana de departamento (`departamentoClave`, `departamentoNombre`).
- [ ] CA-012 `DepartamentoResponse` no expone grafo recursivo completo de empleados por defecto.
- [ ] CA-013 Las pruebas de serializacion confirman ausencia de recursion/ciclos JSON.

### Gate E - Regresion y compatibilidad de iteracion 002

- [ ] CA-014 Los endpoints existentes de empleados mantienen comportamiento funcional esperado tras integrar departamentos.
- [ ] CA-015 Los flujos de autenticacion vigentes no se degradan.
- [ ] CA-016 La aplicacion arranca con migraciones 003 en PostgreSQL dockerizado sin errores de integridad.

### Gate F - Documentacion y cierre

- [ ] CA-017 OpenAPI/Swagger refleja endpoints de departamentos, Bearer JWT y errores 409 de negocio.
- [ ] CA-018 El quickstart de 003 contiene pasos reproducibles de login, CRUD de departamentos y validaciones 404/409.
- [ ] CA-019 Smoke test final dockerizado ejecutado y evidenciado en quickstart.

### Regla Go/No-Go de iteracion

- [ ] CA-020 Go: todos los criterios CA-001..CA-019 en estado cumplido.
- [ ] CA-021 No-Go: si falla cualquier criterio de Gate A o Gate B, se bloquea merge hasta correccion.
