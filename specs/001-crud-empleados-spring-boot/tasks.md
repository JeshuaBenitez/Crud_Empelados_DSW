# Tasks: CRUD de Empleados (API v1)

**Input**: Design documents from `/specs/001-crud-empleados-spring-boot/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`

**Tests**: Se incluyen tareas de pruebas unitarias y de integración para seguridad y paginación, además de cobertura funcional de CRUD.

**Organization**: Tareas agrupadas por historia de usuario para implementación incremental y validación independiente.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencia directa)
- **[Story]**: `US1`, `US2`, `US3`
- Todas las tareas incluyen rutas concretas de archivos

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Ajustes base sobre proyecto existente (sin regenerar)

- [ ] T001 Verificar dependencias necesarias de Spring Boot 3, Security, JPA, Flyway, PostgreSQL y OpenAPI en `DSW02-Practica01/pom.xml`
- [ ] T002 [P] Configurar propiedades de datasource/flyway y credenciales Basic fijas (`admin/admin123`) en `DSW02-Practica01/src/main/resources/application.properties`
- [ ] T003 [P] Validar compatibilidad dockerizada de API + PostgreSQL en `DSW02-Practica01/docker-compose.yml`
- [ ] T004 [P] Actualizar guía de arranque con variables y credenciales académicas en `specs/001-crud-empleados-spring-boot/quickstart.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura mínima para versionado, seguridad y persistencia

**⚠️ CRITICAL**: Ninguna historia inicia antes de completar esta fase

- [ ] T005 Confirmar/ajustar migración de esquema con PK compuesta y secuencia en `DSW02-Practica01/src/main/resources/db/migration/V1__create_empleados_table.sql`
- [ ] T006 [P] Confirmar modelo de PK compuesta (`EmpleadoId`) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/EmpleadoId.java`
- [ ] T007 [P] Confirmar entidad `Empleado` con clave lógica `EMP-<numero>` y validaciones en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/Empleado.java`
- [ ] T008 [P] Confirmar repositorio JPA para PK compuesta y soporte de secuencia en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/repository/EmpleadoRepository.java`
- [ ] T009 Ajustar seguridad HTTP Basic para rutas versionadas `/api/v1/empleados/**` y credenciales fijas en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java`
- [ ] T010 Ajustar manejo global de errores de validación/paginación/autenticación/conflicto en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`
- [ ] T011 Ajustar configuración OpenAPI con esquema Basic y rutas versionadas en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/OpenApiConfig.java`

**Checkpoint**: Base técnica lista para historias de negocio

---

## Phase 3: User Story 1 - API versionada + alta y consulta paginada (Priority: P1) 🎯 MVP

**Goal**: Exponer CRUD base en `/api/v1/empleados`, alta sin clave manual y listado paginado

**Independent Test**: `POST /api/v1/empleados`, `GET /api/v1/empleados/{clave}` y `GET /api/v1/empleados?page=0&size=10` autenticados responden según contrato

### Tests for User Story 1

- [ ] T012 [P] [US1] Crear test unitario de servicio para generación de clave y paginación en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/service/EmpleadoServiceUs1Test.java`
- [ ] T013 [P] [US1] Crear test de integración para alta sin clave y consulta por clave versionada en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoControllerUs1IntegrationTest.java`
- [ ] T014 [P] [US1] Crear test de integración para listado paginado (`page/size`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoPaginationIntegrationTest.java`
- [ ] T033 [P] [US1] Crear test de integración para rechazo de `nombre`, `direccion` o `telefono` con longitud > 100 (`400`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoValidationIntegrationTest.java`
- [ ] T034 [P] [US1] Crear test de integración para rechazo de paginación inválida (`page < 0` o `size > 100`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoPaginationValidationIntegrationTest.java`
- [ ] T035 [P] [US1] Crear test de integración para `409 Conflict` al intentar alta duplicada de PK compuesta en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoConflictIntegrationTest.java`

### Implementation for User Story 1

- [ ] T015 [US1] Actualizar lógica de servicio para listado paginado con defaults `page=0`, `size=10` y validación `size<=100` en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java`
- [ ] T016 [US1] Refactorizar controlador a rutas versionadas `/api/v1/empleados` y parámetros de paginación en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java`
- [ ] T017 [US1] Mantener regla de rechazo de clave manual en alta y `409` por duplicado de PK compuesta en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java`

**Checkpoint**: MVP funcional con API v1 y paginación

---

## Phase 4: User Story 2 - Actualización y eliminación en API v1 (Priority: P2)

**Goal**: Completar mantenimiento de empleados por clave lógica en rutas versionadas

**Independent Test**: `PUT /api/v1/empleados/{clave}` y `DELETE /api/v1/empleados/{clave}` autenticados responden `200` y `204`

### Tests for User Story 2

- [ ] T018 [P] [US2] Crear test unitario para actualización/eliminación por clave en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/service/EmpleadoServiceUs2Test.java`
- [ ] T019 [P] [US2] Crear test de integración para endpoints PUT/DELETE versionados en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoControllerUs2IntegrationTest.java`

### Implementation for User Story 2

- [ ] T020 [US2] Ajustar servicio para actualización/eliminación por clave en flujo API v1 en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java`
- [ ] T021 [US2] Asegurar controlador versionado para PUT/DELETE y respuestas esperadas en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java`
- [ ] T022 [US2] Garantizar `404` consistente para claves inexistentes en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/ResourceNotFoundException.java` y `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`

**Checkpoint**: CRUD completo operativo en API v1

---

## Phase 5: User Story 3 - Seguridad, Swagger y compatibilidad Docker/PostgreSQL (Priority: P3)

**Goal**: Consolidar autenticación fija, documentación OpenAPI y operación dockerizada sin ruptura

**Independent Test**: Acceso sin credenciales retorna `401`, credenciales `admin/admin123` permiten acceso, Swagger refleja API v1 + paginación, y stack dockerizado funciona con PostgreSQL

### Tests for User Story 3

- [ ] T023 [P] [US3] Crear test de seguridad para rechazo sin credenciales y aceptación con `admin/admin123` en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/BasicAuthSecurityTest.java`
- [ ] T024 [P] [US3] Crear test de integración para OpenAPI/Swagger versionado en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/OpenApiVersioningIntegrationTest.java`
- [ ] T025 [P] [US3] Crear test de integración de arranque con PostgreSQL dockerizado en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/PostgresDockerIntegrationTest.java`

### Implementation for User Story 3

- [ ] T026 [US3] Actualizar contrato OpenAPI con rutas `/api/v1/empleados` y parámetros `page/size` en `specs/001-crud-empleados-spring-boot/contracts/empleados-openapi.yaml`
- [ ] T027 [US3] Validar y ajustar configuración Docker de dos contenedores (api + postgres) y red interna en `DSW02-Practica01/docker-compose.yml`
- [ ] T028 [US3] Actualizar quickstart con flujo de prueba en API v1 y autenticación fija en `specs/001-crud-empleados-spring-boot/quickstart.md`

**Checkpoint**: Seguridad, documentación y operación dockerizada verificadas

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad y verificación integral

- [ ] T029 [P] Ejecutar pruebas automáticas de la feature con `mvn test` en `DSW02-Practica01/`
- [ ] T030 Ejecutar compilación final con `mvn -DskipTests compile` en `DSW02-Practica01/`
- [ ] T031 Verificar manualmente criterios SC-001..SC-010 y registrar resultados en `specs/001-crud-empleados-spring-boot/quickstart.md`
- [ ] T032 [P] Ejecutar smoke test dockerizado de API + PostgreSQL y documentar resultado en `specs/001-crud-empleados-spring-boot/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias
- **Phase 3-5 (User Stories)**: dependen de Foundational
- **Phase 6 (Polish)**: depende de finalizar historias objetivo

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational y habilita MVP
- **US2 (P2)**: depende de Foundational y reutiliza base de US1
- **US3 (P3)**: depende de Foundational y valida requisitos transversales (seguridad/docs/docker)

### Within Each User Story

- Pruebas primero (unitarias/integración)
- Servicio antes de controlador
- Contrato/documentación antes del cierre
- Criterios de aceptación completos antes de avanzar

### Parallel Opportunities

- T002, T003 y T004 en paralelo
- T006, T007 y T008 en paralelo
- T012, T013, T014, T033, T034 y T035 en paralelo
- T018 y T019 en paralelo
- T023, T024 y T025 en paralelo
- T029 y T032 en paralelo

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup + Foundational
2. Completar US1 (versionado + paginación + alta/consulta)
3. Validar criterios SC-001, SC-006, SC-008, SC-010
4. Demo de MVP

### Incremental Delivery

1. Entrega 1: API v1 con alta/consulta paginada
2. Entrega 2: actualización y eliminación completas
3. Entrega 3: seguridad/documentación/docker consolidados
4. Cierre: pruebas, compilación y smoke test

### Parallel Team Strategy

1. Equipo completo en Foundational
2. Luego por frentes:
   - Dev A: US1 (versionado+paginación)
   - Dev B: US2 (update/delete)
   - Dev C: US3 (seguridad+OpenAPI+docker)
3. Integración final con validación cruzada
