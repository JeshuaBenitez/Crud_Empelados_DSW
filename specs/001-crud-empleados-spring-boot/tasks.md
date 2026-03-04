# Tasks: CRUD de Empleados

**Input**: Design documents from /specs/001-crud-empleados-spring-boot/  
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/

**Tests**: Se incluyen tareas de pruebas porque la constitución exige evidencia verificable y trazabilidad de calidad.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Format: [ID] [P?] [Story] Description

- [P]: Puede ejecutarse en paralelo (archivos distintos, sin dependencia directa)
- [Story]: US1, US2 o US3
- Cada tarea incluye ruta exacta de archivo

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicialización del backend y dependencias base

- [ ] T001 Configurar dependencias Spring Boot 3 + Java 17 + Security + JPA + Validation + Flyway + PostgreSQL + Springdoc en DSW02-Practica01/pom.xml
- [ ] T002 Crear clase de arranque en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/Dsw02Practica01Application.java
- [ ] T003 [P] Configurar propiedades base de datasource/JPA/Flyway/Swagger en DSW02-Practica01/src/main/resources/application.properties
- [ ] T004 [P] Configurar PostgreSQL local en Docker en DSW02-Practica01/docker-compose.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura obligatoria antes de implementar historias

**⚠️ CRITICAL**: Ninguna historia inicia antes de completar esta fase

- [ ] T005 Crear migración inicial de tabla empleados con PK compuesta (prefijo, numero), secuencia autonumérica y campos VARCHAR(100) en DSW02-Practica01/src/main/resources/db/migration/V1__create_empleados_table.sql
- [ ] T006 [P] Implementar clase de PK compuesta para Empleado en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/EmpleadoId.java
- [ ] T007 [P] Implementar entidad Empleado con @EmbeddedId, clave lógica derivada EMP-<numero> y validaciones @NotBlank + @Size(max=100) en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/Empleado.java
- [ ] T008 [P] Implementar repositorio JPA para PK compuesta en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/repository/EmpleadoRepository.java
- [ ] T009 [P] Implementar excepción de negocio para no encontrado en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/ResourceNotFoundException.java
- [ ] T010 Implementar manejo global de errores (400/404/409/500) incluyendo generación fallida de clave en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java
- [ ] T011 Implementar seguridad HTTP Basic para /api/empleados/** y exclusión de Swagger en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java
- [ ] T012 Implementar configuración OpenAPI con basicAuth en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/OpenApiConfig.java

**Checkpoint**: Base lista para implementar historias en orden de prioridad o en paralelo por equipo

---

## Phase 3: User Story 1 - Alta y consulta de empleados (Priority: P1) 🎯 MVP

**Goal**: Crear empleados sin enviar clave y consultarlos por clave lógica EMP-<numero>

**Independent Test**: POST /api/empleados (sin clave), GET /api/empleados/{clave} y GET /api/empleados autenticados responden según contrato

### Tests for User Story 1

- [ ] T013 [P] [US1] Crear test de integración para alta sin clave y consulta por clave EMP-<numero> en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoControllerUs1IntegrationTest.java
- [ ] T014 [P] [US1] Crear test de contrato para endpoints US1 en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/contract/EmpleadoContractUs1Test.java

### Implementation for User Story 1

- [ ] T015 [US1] Implementar servicio de alta con generación de clave lógica EMP-<numero> a partir de secuencia/numero y consultas por clave en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java
- [ ] T016 [US1] Implementar endpoints POST/GET list/GET by clave para modelo EMP-<numero> en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java
- [ ] T017 [US1] Rechazar clave manual en payload de alta y devolver 400 cuando no cumpla reglas de entrada en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java y DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java
- [ ] T018 [US1] Implementar respuesta 409 Conflict para duplicado de PK compuesta (prefijo, numero) en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java y DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java

**Checkpoint**: US1 funcional y demostrable como MVP

---

## Phase 4: User Story 2 - Actualización y eliminación de empleados (Priority: P2)

**Goal**: Permitir mantenimiento de datos existentes por clave EMP-<numero>

**Independent Test**: PUT /api/empleados/{clave} y DELETE /api/empleados/{clave} autenticados responden 200 y 204

### Tests for User Story 2

- [ ] T019 [P] [US2] Crear test de integración de actualización/eliminación por clave EMP-<numero> en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/EmpleadoControllerUs2IntegrationTest.java
- [ ] T020 [P] [US2] Crear test de contrato para endpoints US2 en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/contract/EmpleadoContractUs2Test.java

### Implementation for User Story 2

- [ ] T021 [US2] Extender servicio con operaciones de actualización y eliminación por clave EMP-<numero> en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java
- [ ] T022 [US2] Implementar endpoints PUT/DELETE con resolución interna a PK compuesta en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java
- [ ] T023 [US2] Garantizar 404 Not Found para claves EMP-<numero> inexistentes vía ResourceNotFoundException y GlobalExceptionHandler

**Checkpoint**: US1 y US2 funcionan de manera independiente

---

## Phase 5: User Story 3 - Seguridad, PostgreSQL Docker y Swagger (Priority: P3)

**Goal**: Garantizar operación segura, persistencia reproducible y documentación API

**Independent Test**: Solicitudes sin credenciales devuelven 401, app inicia con PostgreSQL Docker y Swagger UI expone contrato CRUD con clave EMP-<numero>

### Tests for User Story 3

- [ ] T024 [P] [US3] Crear test de seguridad para acceso no autenticado en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/BasicAuthSecurityTest.java
- [ ] T025 [P] [US3] Crear test de arranque con Flyway y datasource PostgreSQL (perfil de integración) en DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/PostgresFlywayIntegrationTest.java

### Implementation for User Story 3

- [ ] T026 [US3] Ajustar credenciales y propiedades para no hardcodear secretos en DSW02-Practica01/src/main/resources/application.properties y documentación en specs/001-crud-empleados-spring-boot/quickstart.md
- [ ] T027 [US3] Publicar y alinear contrato OpenAPI con clave EMP-<numero> en specs/001-crud-empleados-spring-boot/contracts/empleados-openapi.yaml
- [ ] T028 [US3] Verificar y ajustar configuración Swagger UI + scheme Basic en DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/OpenApiConfig.java

**Checkpoint**: Seguridad, persistencia y documentación completas y verificadas

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad y consistencia global

- [ ] T029 [P] Actualizar guía operativa y ejemplos curl con clave EMP-<numero> en specs/001-crud-empleados-spring-boot/quickstart.md
- [ ] T030 [P] Ejecutar suite de pruebas y corregir fallos de la feature con mvn test en DSW02-Practica01/
- [ ] T031 Ejecutar validación de build final con mvn -DskipTests compile en DSW02-Practica01/
- [ ] T032 Validar manualmente criterios SC-001..SC-007 y registrar resultado en specs/001-crud-empleados-spring-boot/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias
- **Phases 3-5 (User Stories)**: dependen de Phase 2
- **Phase 6 (Polish)**: depende de completar historias seleccionadas

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational
- **US2 (P2)**: depende de Foundational; reutiliza componentes de US1 sin bloquear su prueba independiente
- **US3 (P3)**: depende de Foundational; valida requisitos transversales sobre endpoints existentes

### Within Each User Story

- Pruebas primero y en fallo esperado (si aplica TDD)
- Servicio antes de controlador
- Manejo de errores y validaciones antes del cierre de historia
- Criterio de historia cumplido antes de pasar a la siguiente

### Parallel Opportunities

- T003 y T004 en paralelo
- T006, T007, T008 y T009 en paralelo
- T013 y T014 en paralelo
- T019 y T020 en paralelo
- T024 y T025 en paralelo
- T029 y T030 en paralelo

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 + Phase 2
2. Completar Phase 3 (US1)
3. Validar SC-001 parcial y SC-006
4. Demo interna del MVP

### Incremental Delivery

1. US1 listo y validado
2. Añadir US2 y validar regresión de US1
3. Añadir US3 y validar seguridad + documentación + operación con Docker
4. Cerrar con polish y verificación final

### Parallel Team Strategy

1. Todo el equipo cierra Setup + Foundational
2. Luego:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integración final y verificación de criterios de éxito
