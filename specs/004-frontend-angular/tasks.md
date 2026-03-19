# Tasks: Frontend Angular para Login y CRUD de Empleados/Departamentos

**Input**: Design documents from `/specs/004-frontend-angular/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required)

**Tests**: Se incluyen tareas de pruebas E2E con Cypress y validacion de integracion con backend existente.

**Organization**: Tareas agrupadas por fases ejecutables, manteniendo implementacion incremental y sin regenerar backend.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos y sin dependencia directa)
- **[Story]**: `US1`, `US2`, `US3`, `US4`, `US5`
- Cada tarea incluye rutas concretas de archivo/directorio

---

## Phase 1: Setup del Proyecto Angular (Shared Infrastructure)

**Purpose**: Preparar base tecnica del frontend en `frontend-angular/`.

- [ ] T001 Inicializar estructura base Angular 22 LTS en `frontend-angular/` (sin afectar `DSW02-Practica01/`)
- [ ] T002 [P] Configurar Tailwind CSS global y su integracion en `frontend-angular/src/styles.css` + archivos de configuracion frontend
- [ ] T003 [P] Configurar entornos y URL base API en `frontend-angular/src/environments/environment.ts` y `frontend-angular/src/environments/environment.prod.ts`
- [ ] T004 [P] Preparar estructura de carpetas `core/shared/features` en `frontend-angular/src/app/`
- [ ] T005 [P] Preparar Cypress en `frontend-angular/cypress/` y scripts de ejecucion E2E en configuracion del frontend
- [ ] T006 Definir convencion de selectores de testing (`data-testid`) en componentes de `frontend-angular/src/app/`

**Checkpoint**: Frontend listo para construir features sin bloquear integracion futura.

---

## Phase 2: Autenticacion y Sesion JWT (Blocking Prerequisites)

**Purpose**: Implementar seguridad frontend base para rutas protegidas.

**⚠️ CRITICAL**: Ningun modulo de negocio inicia antes de cerrar esta fase.

### Tests for Auth Foundation

- [ ] T007 [P] [US1] Definir casos E2E de login exitoso/fallido (estructura base) en `frontend-angular/cypress/e2e/auth/`

### Implementation for Auth Foundation

- [ ] T008 [US1] Implementar `AuthService` (login + estado de sesion) en `frontend-angular/src/app/core/auth/`
- [ ] T009 [US1] Implementar almacenamiento JWT en `sessionStorage` y utilidades de sesion en `frontend-angular/src/app/core/auth/`
- [ ] T010 [US1] Implementar `AuthGuard` para rutas protegidas en `frontend-angular/src/app/core/auth/`
- [ ] T011 [US1] Implementar logout explicito (limpieza de sesion + redireccion) en `frontend-angular/src/app/core/layout/` y `frontend-angular/src/app/core/auth/`
- [ ] T012 [US1] Implementar interceptor HTTP para Bearer token y manejo global de `401` en `frontend-angular/src/app/core/http/`

**Checkpoint**: Login/sesion/rutas protegidas operativas con backend real.

---

## Phase 3: Layout y Navegacion (Public/Protected Routing)

**Purpose**: Construir experiencia de navegacion clara y consistente.

### Tests for Layout & Routing

- [ ] T013 [P] [US4] Definir E2E de navegacion entre login, dashboard, empleados y departamentos en `frontend-angular/cypress/e2e/navigation/`

### Implementation for Layout & Routing

- [ ] T014 [US4] Definir rutas publicas y protegidas en `frontend-angular/src/app/app.routes.ts`
- [ ] T015 [US4] Implementar pantalla de login en `frontend-angular/src/app/features/login/`
- [ ] T016 [US4] Implementar dashboard inicial autenticado en `frontend-angular/src/app/features/dashboard/`
- [ ] T017 [US4] Implementar layout comun autenticado con navbar + sidebar en `frontend-angular/src/app/core/layout/`
- [ ] T018 [US4] Integrar flujo de redireccion post-login y acceso bloqueado a rutas privadas sin sesion en `frontend-angular/src/app/`

**Checkpoint**: Navegacion principal cerrada con separacion clara de rutas.

---

## Phase 4: Modulo de Empleados (US2, Priority: P1) 🎯

**Goal**: Habilitar listado, alta/edicion y eliminacion de empleados desde frontend.

**Independent Test**: CRUD representativo de empleados desde UI usando JWT.

### Tests for User Story 2

- [ ] T019 [P] [US2] Crear E2E de listado de empleados en `frontend-angular/cypress/e2e/empleados/empleados-list.cy.ts`
- [ ] T020 [P] [US2] Crear E2E de crear/editar empleado en `frontend-angular/cypress/e2e/empleados/empleados-form.cy.ts`
- [ ] T021 [P] [US2] Crear E2E de eliminacion y manejo de error de negocio en `frontend-angular/cypress/e2e/empleados/empleados-delete.cy.ts`

### Implementation for User Story 2

- [ ] T022 [US2] Implementar `EmpleadosService` para consumo API en `frontend-angular/src/app/features/empleados/data-access/` (o equivalente)
- [ ] T023 [US2] Implementar vista de listado paginado de empleados en `frontend-angular/src/app/features/empleados/`
- [ ] T024 [US2] Implementar formulario de alta/edicion en ruta separada en `frontend-angular/src/app/features/empleados/`
- [ ] T025 [US2] Implementar accion de eliminacion con confirmacion y refresco de listado en `frontend-angular/src/app/features/empleados/`
- [ ] T026 [US2] Integrar mapeo de errores backend (`403/409/404`) a feedback UI en `frontend-angular/src/app/features/empleados/`

**Checkpoint**: Modulo de empleados funcional contra backend existente.

---

## Phase 5: Modulo de Departamentos (US3, Priority: P1)

**Goal**: Habilitar listado, alta/edicion y eliminacion de departamentos desde frontend.

**Independent Test**: CRUD representativo de departamentos con manejo de conflictos (`409`).

### Tests for User Story 3

- [ ] T027 [P] [US3] Crear E2E de listado de departamentos en `frontend-angular/cypress/e2e/departamentos/departamentos-list.cy.ts`
- [ ] T028 [P] [US3] Crear E2E de crear/editar departamento en `frontend-angular/cypress/e2e/departamentos/departamentos-form.cy.ts`
- [ ] T029 [P] [US3] Crear E2E de eliminacion con conflicto de negocio (`409`) en `frontend-angular/cypress/e2e/departamentos/departamentos-delete.cy.ts`

### Implementation for User Story 3

- [ ] T030 [US3] Implementar `DepartamentosService` para consumo API en `frontend-angular/src/app/features/departamentos/data-access/` (o equivalente)
- [ ] T031 [US3] Implementar vista de listado paginado de departamentos en `frontend-angular/src/app/features/departamentos/`
- [ ] T032 [US3] Implementar formulario de alta/edicion en ruta separada en `frontend-angular/src/app/features/departamentos/`
- [ ] T033 [US3] Implementar accion de eliminacion con confirmacion y manejo de `409` en `frontend-angular/src/app/features/departamentos/`
- [ ] T034 [US3] Integrar mensajes de error/negocio alineados a backend en `frontend-angular/src/app/features/departamentos/`

**Checkpoint**: Modulo de departamentos funcional y coherente con reglas backend.

---

## Phase 6: UX, Estados y Consistencia Visual (US4, Priority: P2)

**Purpose**: Garantizar experiencia limpia, predecible y consistente en toda la app.

### Tests for UX

- [ ] T035 [P] [US4] Agregar validaciones E2E de estados `loading/error/empty` en `frontend-angular/cypress/e2e/ui-states/`

### Implementation for UX

- [ ] T036 [US4] Implementar componentes reutilizables de `loading`, `empty`, `error` en `frontend-angular/src/app/shared/components/`
- [ ] T037 [US4] Implementar sistema de notificaciones (exito/error) reutilizable en `frontend-angular/src/app/shared/components/` (o `core/`)
- [ ] T038 [US4] Aplicar patron comun `tabla + acciones + formulario` en empleados/departamentos en `frontend-angular/src/app/features/`
- [ ] T039 [US4] Homologar estilos y tokens de UI con Tailwind CSS en `frontend-angular/src/`
- [ ] T040 [US4] Verificar responsive basico (desktop y mobile) en vistas clave de `frontend-angular/src/app/features/`

**Checkpoint**: UX consistente y alineada a lineamientos de iteracion 004.

---

## Phase 7: Pruebas E2E Cypress (US5, Priority: P2)

**Purpose**: Cubrir regresiones criticas de autenticacion y CRUD.

- [ ] T041 [US5] Consolidar suite E2E de autenticacion (login ok/fail + logout) en `frontend-angular/cypress/e2e/auth/`
- [ ] T042 [US5] Consolidar suite E2E de empleados (listado + formulario + eliminacion) en `frontend-angular/cypress/e2e/empleados/`
- [ ] T043 [US5] Consolidar suite E2E de departamentos (listado + formulario + eliminacion/conflicto) en `frontend-angular/cypress/e2e/departamentos/`
- [ ] T044 [US5] Configurar datos/fixtures y utilidades comunes de Cypress en `frontend-angular/cypress/support/`
- [ ] T045 [US5] Documentar ejecucion local y criterios de pase de E2E en `specs/004-frontend-angular/quickstart.md`

**Checkpoint**: Base de automatizacion E2E estable para iteraciones siguientes.

---

## Phase 8: Validacion Final e Integracion Completa

**Purpose**: Cerrar iteracion con evidencia de integracion real frontend-backend.

- [ ] T046 [P] Ejecutar smoke test de autenticacion y navegacion protegida (login -> dashboard -> empleados/departamentos -> logout) y registrar evidencia en `specs/004-frontend-angular/quickstart.md`
- [ ] T047 [P] Ejecutar smoke test CRUD representativo con backend real para empleados y departamentos y registrar evidencia en `specs/004-frontend-angular/quickstart.md`
- [ ] T048 Validar que no se hicieron cambios innecesarios en backend (`DSW02-Practica01/`) para cerrar 004 y registrar resultado en `specs/004-frontend-angular/quickstart.md`
- [ ] T049 Validar cumplimiento de FR y SC de `spec.md` y registrar checklist final en `specs/004-frontend-angular/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia de inmediato.
- **Phase 2 (Auth Foundation)**: depende de Phase 1 y bloquea modulos de negocio.
- **Phase 3 (Layout/Routing)**: depende de Phase 2.
- **Phase 4 (Empleados)** y **Phase 5 (Departamentos)**: dependen de Phase 2 y pueden avanzar en paralelo tras cerrar rutas base.
- **Phase 6 (UX)**: puede avanzar en paralelo con fases 4/5 tras tener vistas base.
- **Phase 7 (Cypress)**: se consolida cuando 3/4/5 estan funcionales.
- **Phase 8 (Validacion Final)**: depende de cierre funcional y pruebas.

### Parallel Opportunities

- `T002`, `T003`, `T004`, `T005` en paralelo en setup.
- `T019..T021` en paralelo entre si (empleados E2E).
- `T027..T029` en paralelo entre si (departamentos E2E).
- `T046` y `T047` en paralelo controlado en cierre.

---

## Implementation Strategy

### MVP First

1. Completar setup + auth foundation.
2. Cerrar login + rutas protegidas + layout comun.
3. Entregar modulo empleados operativo.

### Incremental Delivery

1. Entrega 1: infraestructura frontend + seguridad JWT.
2. Entrega 2: navegacion y layout autenticado.
3. Entrega 3: modulo empleados.
4. Entrega 4: modulo departamentos.
5. Entrega 5: UX transversal + Cypress + validacion final.

### Safety Gates

- No iniciar CRUD de negocio sin guard/interceptor/sesion operativos.
- No cerrar iteracion sin evidencia E2E minima (auth + empleados + departamentos).
- No aprobar merge si se detectan cambios backend fuera de incompatibilidades contractuales justificadas.

---

## Criterios de Aceptacion Operativos (derivados de spec/clarify)

### Gate A - Seguridad frontend

- [ ] CA-001 JWT se almacena en `sessionStorage` y se limpia en logout.
- [ ] CA-002 Rutas protegidas redirigen a login sin sesion valida.
- [ ] CA-003 Interceptor adjunta Bearer token y maneja `401` global.

### Gate B - Navegacion y layout

- [ ] CA-004 Navegacion principal `login -> dashboard -> empleados/departamentos` funcional.
- [ ] CA-005 Layout autenticado con navbar y sidebar operativo.

### Gate C - Modulos de negocio

- [ ] CA-006 CRUD representativo de empleados funcionando contra backend real.
- [ ] CA-007 CRUD representativo de departamentos funcionando contra backend real.
- [ ] CA-008 Errores `403/404/409` visibles con mensajes claros.

### Gate D - UX y pruebas

- [ ] CA-009 Estados `loading/error/empty` implementados en vistas clave.
- [ ] CA-010 Consistencia visual con Tailwind aplicada a login, empleados y departamentos.
- [ ] CA-011 Cypress cubre login + empleados + departamentos en ejecucion local.

### Gate E - Cierre de iteracion

- [ ] CA-012 Integracion completa frontend-backend validada por smoke tests.
- [ ] CA-013 No se regenera ni rehace arquitectura backend.
- [ ] CA-014 Evidencia final documentada en `specs/004-frontend-angular/quickstart.md`.
