# Tasks: Autenticacion de Empleados por Correo y Password

**Input**: Design documents from `/specs/002-crud-empleados-spring-boot/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`

**Tests**: Se incluyen tareas de pruebas unitarias y de integracion porque la especificacion define criterios medibles de autenticacion, autorizacion y no-regresion.

**Organization**: Tareas agrupadas por historia de usuario para implementar y validar cada incremento de forma independiente.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos y sin dependencia directa)
- **[Story]**: `US1`, `US2`, `US3`
- Cada tarea incluye rutas de archivo concretas

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar dependencias y configuracion base para autenticacion en iteracion 002

- [ ] T001 Agregar y fijar dependencia JWT compatible con Spring Boot 3/Java 17 en `DSW02-Practica01/pom.xml`
- [ ] T002 [P] Agregar propiedades de JWT (secret, expiracion) y mantener compatibilidad con Basic admin en `DSW02-Practica01/src/main/resources/application.properties`
- [ ] T003 [P] Verificar variables de entorno requeridas para JWT y seguridad en `DSW02-Practica01/docker-compose.yml`
- [ ] T004 [P] Crear paquete de seguridad para componentes JWT en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/security/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de seguridad y persistencia que bloquea todas las historias

**⚠️ CRITICAL**: Ninguna historia inicia hasta cerrar esta fase

- [ ] T005 Crear migracion para autenticacion de empleados (correo unico, password hash, estado) en `DSW02-Practica01/src/main/resources/db/migration/V2__add_empleado_auth_columns.sql`
- [ ] T005A Crear script obligatorio de carga previa de datos para migracion de autenticacion en `DSW02-Practica01/src/main/resources/db/migration/V1_1__preload_empleado_auth_data.sql`
- [ ] T005B Crear seed inicial para pruebas de login/autorizacion en `DSW02-Practica01/src/main/resources/db/migration/V2_1__seed_empleado_auth_test_data.sql`
- [ ] T006 [P] Extender modelo de empleado con campos de autenticacion en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/model/Empleado.java`
- [ ] T007 [P] Extender repositorio para busqueda por correo en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/repository/EmpleadoRepository.java`
- [ ] T008 [P] Implementar proveedor/utilidad JWT (generacion y validacion) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/security/JwtTokenProvider.java`
- [ ] T009 Implementar filtro de autenticacion Bearer JWT en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/security/JwtAuthenticationFilter.java`
- [ ] T010 Ajustar reglas de seguridad para JWT only en `/api/v1/empleados/**` y Basic solo en rutas administrativas en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java`
- [ ] T011 Ajustar manejo global de errores para login/authorization (`400`, `401`) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`

**Checkpoint**: Base de seguridad y persistencia lista para historias de autenticacion

---

## Phase 3: User Story 1 - Login de empleado por correo/password (Priority: P1) 🎯 MVP

**Goal**: Permitir autenticacion de empleado contra PostgreSQL mediante `POST /api/v1/auth/empleados/login`

**Independent Test**: Login exitoso con credenciales validas devuelve token; login invalido devuelve `401`; payload invalido devuelve `400`

### Tests for User Story 1

- [ ] T012 [P] [US1] Crear test unitario de servicio de autenticacion (password hash y validacion) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/service/AuthServiceUs1Test.java`
- [ ] T013 [P] [US1] Crear test de integracion para login exitoso en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/AuthControllerLoginSuccessIntegrationTest.java`
- [ ] T014 [P] [US1] Crear test de integracion para login fallido (`401`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/AuthControllerLoginFailureIntegrationTest.java`
- [ ] T015 [P] [US1] Crear test de integracion para payload invalido (`400`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/controller/AuthControllerLoginValidationIntegrationTest.java`

### Implementation for User Story 1

- [ ] T016 [P] [US1] Crear DTO de request login (`correo`, `password`) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/dto/LoginEmpleadoRequest.java`
- [ ] T017 [P] [US1] Crear DTO de response login (token, tipo, expiracion) en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/dto/LoginEmpleadoResponse.java`
- [ ] T018 [US1] Implementar servicio de autenticacion por correo/password hash en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/AuthService.java`
- [ ] T019 [US1] Implementar endpoint `POST /api/v1/auth/empleados/login` en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/AuthController.java`
- [ ] T020 [US1] Garantizar mensajes de error no reveladores en fallas de login en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/AuthService.java` y `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/exception/GlobalExceptionHandler.java`

**Checkpoint**: MVP de autenticacion (login + emision de token) funcional y validable

---

## Phase 4: User Story 2 - Autorizacion con Bearer JWT en endpoints de empleados (Priority: P2)

**Goal**: Proteger `/api/v1/empleados/**` con JWT only, mantener Basic solo en admin y aplicar autorizacion de recurso propio

**Independent Test**: Endpoint de empleados responde `2xx` con token valido sobre recurso propio, `401` con token invalido/ausente y `403` en recurso de tercero

### Tests for User Story 2

- [ ] T021 [P] [US2] Crear test de seguridad para acceso con token valido en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/JwtProtectedEndpointSuccessIntegrationTest.java`
- [ ] T022 [P] [US2] Crear test de seguridad para token ausente/invalido/expirado (`401`) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/JwtProtectedEndpointFailureIntegrationTest.java`
- [ ] T023 [P] [US2] Crear test de no-regresion de Basic admin restringido a rutas administrativas en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/BasicAdminCompatibilityIntegrationTest.java`
- [ ] T023A [P] [US2] Crear test de autorizacion de recurso propio (`403` en recurso de tercero) en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/security/OwnResourceAuthorizationIntegrationTest.java`

### Implementation for User Story 2

- [ ] T024 [US2] Integrar filtro JWT en cadena de seguridad sin romper Basic admin en rutas administrativas en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java`
- [ ] T025 [US2] Aplicar reglas JWT only en `/api/v1/empleados/**`, rutas publicas a login/swagger y Basic solo admin en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/SecurityConfig.java`
- [ ] T026 [US2] Ajustar servicio/controlador de empleados para validar acceso solo a recurso propio y retornar `403` para terceros en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/controller/EmpleadoController.java` y `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/service/EmpleadoService.java`

**Checkpoint**: Endpoints de empleados protegidos por JWT y compatibilidad administrativa conservada

---

## Phase 5: User Story 3 - Swagger usable y operacion Docker/PostgreSQL (Priority: P3)

**Goal**: Documentar y validar autenticacion JWT en OpenAPI manteniendo ejecucion local reproducible

**Independent Test**: Swagger permite probar login + consumo autenticado, y app arranca con Docker + PostgreSQL

### Tests for User Story 3

- [ ] T027 [P] [US3] Crear test de integracion para disponibilidad de Swagger/OpenAPI con esquema Bearer en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/OpenApiJwtIntegrationTest.java`
- [ ] T028 [P] [US3] Crear test de arranque con PostgreSQL dockerizado y login habilitado en `DSW02-Practica01/src/test/java/com/dwgabo/dsw02practica01/integration/PostgresAuthStartupIntegrationTest.java`

### Implementation for User Story 3

- [ ] T029 [US3] Actualizar configuracion OpenAPI para esquema Bearer JWT y endpoint login en `DSW02-Practica01/src/main/java/com/dwgabo/dsw02practica01/config/OpenApiConfig.java`
- [ ] T030 [US3] Actualizar contrato OpenAPI de autenticacion en `specs/002-crud-empleados-spring-boot/contracts/auth-empleados-openapi.yaml`
- [ ] T031 [US3] Actualizar guia operativa de login/JWT en `specs/002-crud-empleados-spring-boot/quickstart.md`

**Checkpoint**: Documentacion y operacion local listas para validacion de feature 002

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, trazabilidad y control de alcance

- [ ] T032 [P] Ejecutar suite de pruebas de autenticacion y regresion con `mvn test` en `DSW02-Practica01/`
- [ ] T033 Ejecutar compilacion final con `mvn -DskipTests compile` en `DSW02-Practica01/`
- [ ] T034 Verificar manualmente SC-001..SC-008 y registrar evidencias en `specs/002-crud-empleados-spring-boot/quickstart.md`
- [ ] T035 [P] Ejecutar smoke test dockerizado (login + acceso protegido) y registrar resultado en `specs/002-crud-empleados-spring-boot/quickstart.md`
- [ ] T036 Validar explicitamente que no se agregaron endpoints/modelos de departamentos en iteracion 002 y registrar resultado en `specs/002-crud-empleados-spring-boot/quickstart.md`
- [ ] T037 Validar ejecucion del script de carga obligatoria y seed inicial en flujo de migracion local en `specs/002-crud-empleados-spring-boot/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias
- **Phase 3-5 (User Stories)**: dependen de Foundational
- **Phase 6 (Polish)**: depende de completar historias objetivo

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational y habilita MVP de login
- **US2 (P2)**: depende de Foundational y del token emitido en US1
- **US3 (P3)**: depende de Foundational; valida documentacion y operacion final

### Within Each User Story

- Tests primero (fallo esperado)
- DTO/modelo antes de servicio
- Servicio antes de controlador/filtro
- Seguridad y errores antes del cierre de historia
- Criterios de historia cumplidos antes de avanzar

### Parallel Opportunities

- T002, T003 y T004 en paralelo
- T006, T007 y T008 en paralelo
- T012, T013, T014 y T015 en paralelo
- T021, T022, T023 y T023A en paralelo
- T027 y T028 en paralelo
- T032 y T035 en paralelo

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup + Foundational
2. Completar US1 (login por correo/password + JWT)
3. Validar SC-001, SC-002, SC-003
4. Demo de login funcional

### Incremental Delivery

1. Entrega 1: Login JWT operativo (US1)
2. Entrega 2: Proteccion de endpoints por Bearer (US2)
3. Entrega 3: Swagger + Docker/PostgreSQL validados (US3)
4. Cierre: pruebas completas, smoke test y verificacion de no-alcance a departamentos

### Parallel Team Strategy

1. Equipo completo en Foundational
2. Luego por frentes:
   - Dev A: US1 (login + token)
   - Dev B: US2 (filtros y reglas de acceso)
   - Dev C: US3 (OpenAPI/quickstart/operacion)
3. Integracion final y verificacion cruzada
