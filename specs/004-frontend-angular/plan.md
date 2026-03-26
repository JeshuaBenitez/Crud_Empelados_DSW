# Implementation Plan: Frontend Angular para Login y CRUD de Empleados/Departamentos

**Branch**: `004-frontend-angular` | **Date**: 2026-03-19 | **Spec**: `/specs/004-frontend-angular/spec.md`  
**Input**: Feature specification from `/specs/004-frontend-angular/spec.md`

## Summary

Implementar de forma incremental un frontend en Angular 22 LTS dentro de `frontend-angular/`, con UI en Tailwind CSS y base de pruebas E2E en Cypress, consumiendo el backend existente (JWT login de empleados + CRUD de empleados + CRUD de departamentos) sin regenerar ni modificar innecesariamente el backend. El frontend incorporara autenticacion por JWT, rutas protegidas, layout autenticado comun, modulos de empleados y departamentos con patron compartido de tabla/formulario, y manejo consistente de errores/estados.

## Technical Context

**Language/Version**: TypeScript + Angular 22 LTS  
**Primary Dependencies**: Angular Router, Angular HttpClient, Tailwind CSS, Cypress  
**Storage**: Session state en navegador (`sessionStorage` para JWT)  
**Testing**: Cypress E2E (flujos criticos) + pruebas unitarias Angular segun necesidad de componentes/servicios  
**Target Platform**: Navegadores modernos (desktop-first, responsive mobile)  
**Project Type**: Frontend SPA integrado a backend monolitico existente  
**Performance Goals**: Carga inicial y navegacion fluida para CRUD basico (sin SLO cuantitativo nuevo en esta iteracion)  
**Constraints**:
- Frontend obligatorio en `frontend-angular/`
- Integracion exclusiva con API backend existente `/api/v1/...`
- JWT en `sessionStorage` + `AuthGuard` + interceptor Bearer
- Layout autenticado comun (`navbar` + `sidebar`)
- Formularios en rutas separadas (no modales)
- Tailwind CSS como sistema base de estilos
- Cypress para login + flujos CRUD representativos
- Sin regenerar backend ni reestructurar arquitectura existente
**Scale/Scope**: Iteracion funcional completa de frontend para login, empleados y departamentos

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Runtime Base Inmutable y Bimodal**: PASS. Se mantiene backend Spring Boot/Java 17 y se agrega frontend Angular 22 LTS en carpeta dedicada.
- **II. Seguridad Evolutiva y Compatibilidad por Iteraciones**: PASS. Frontend usa login de empleados existente y JWT vigente, sin mecanismos paralelos.
- **III. Persistencia PostgreSQL Containerizada**: PASS. Frontend consume backend sin alterar estrategia de persistencia ni Docker actual.
- **IV. Contrato API Versionado en Swagger**: PASS. Consumo de endpoints versionados existentes, alineado a contratos ya publicados.
- **V. Calidad Verificable, Paginacion y Trazabilidad**: PASS. Se planifican estados UI, pruebas E2E Cypress y trazabilidad de decisiones en artefactos.
- **Reglas de Evolucion Frontend (Iteración 004)**: PASS. Expansion incremental del repositorio sin reemplazo del backend.

## Project Structure

### Documentation (this feature)

```text
specs/004-frontend-angular/
├── spec.md
├── plan.md
├── quickstart.md            # pendiente en fase de documentacion
├── contracts/               # opcional para mock/contratos frontend
└── tasks.md                 # pendiente en /speckit.tasks
```

### Source Code (repository root)

```text
DW_Gabo/
├── DSW02-Practica01/        # backend existente (sin regeneracion)
├── frontend-angular/        # frontend iteracion 004
└── specs/
    └── 004-frontend-angular/
```

### Frontend Proposed Structure

```text
frontend-angular/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── auth/                # auth service, guard, session model
│   │   │   ├── http/                # api client base + interceptor
│   │   │   └── layout/              # layout autenticado comun
│   │   ├── shared/
│   │   │   ├── components/          # tabla, estados vacio/error/loading, feedback
│   │   │   └── models/
│   │   ├── features/
│   │   │   ├── login/
│   │   │   ├── dashboard/
│   │   │   ├── empleados/
│   │   │   └── departamentos/
│   │   ├── app.routes.ts
│   │   └── app.config.ts
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   └── styles.css                 # Tailwind entrypoint
└── cypress/
    ├── e2e/
    └── support/
```

**Structure Decision**: Arquitectura por features con `core/shared/features` para separar infraestructura transversal (auth/http/layout) de modulos de negocio (empleados/departamentos).

## Implementation Strategy

### Phase 0 - Research & Integration Validation

1. Verificar contrato real de endpoints backend para login, empleados y departamentos (payloads, codigos y errores).
2. Confirmar estrategia CORS y URL base para entorno local (API en host/puerto existente).
3. Confirmar estrategia de sesion definida en spec: JWT en `sessionStorage` + limpieza en logout/401.
4. Revisar restricciones actuales backend que impactan UX (por ejemplo operaciones restringidas con `403`).
5. Definir baseline visual con Tailwind y criterios de consistencia entre modulos.

### Phase 1 - Frontend Foundation (Angular + Tailwind)

1. Preparar estructura Angular 22 LTS en `frontend-angular/` (sin afectar backend).
2. Configurar Tailwind CSS como sistema base global.
3. Configurar `environments` para URL base API por entorno.
4. Definir rutas base (`/login`, `/dashboard`, `/empleados`, `/departamentos`).
5. Montar layout autenticado comun con navbar y sidebar colapsable.

### Phase 2 - Security & Session Layer

1. Implementar `AuthService` para login y ciclo de sesion.
2. Implementar almacenamiento de JWT en `sessionStorage`.
3. Implementar `AuthGuard` para rutas protegidas.
4. Implementar interceptor HTTP:
- Inyeccion de Bearer token en requests protegidos.
- Manejo global de `401` con limpieza de sesion y redireccion a login.
5. Implementar accion de logout explicito en layout autenticado.

### Phase 3 - API Consumption Layer

1. Definir cliente HTTP compartido y normalizacion de errores.
2. Implementar `EmpleadosService` con operaciones de listado/alta/edicion/eliminacion segun backend.
3. Implementar `DepartamentosService` con operaciones de listado/alta/edicion/eliminacion segun backend.
4. Estandarizar mapping de parametros de paginacion (`page`, `size`) y manejo de respuestas.
5. Definir politica de reintento basica para fallos transitorios (cuando aplique).

### Phase 4 - Feature Modules & Views

1. **Login**
- Vista de login con validaciones de campos.
- Manejo de errores de autenticacion (400/401) con mensajes claros.
- Redireccion a dashboard al autenticar.

2. **Dashboard**
- Vista de aterrizaje autenticada con accesos a empleados y departamentos.

3. **Empleados**
- Vista de listado paginado.
- Vista de formulario crear/editar por ruta separada.
- Accion eliminar con confirmacion.
- Manejo de respuestas `403`/`409` segun reglas backend.

4. **Departamentos**
- Vista de listado paginado.
- Vista de formulario crear/editar por ruta separada.
- Accion eliminar con confirmacion.
- Manejo de conflictos de negocio (`409`) y no encontrados (`404`).

### Phase 5 - Shared UX Patterns (Clean UI)

1. Crear componentes reutilizables para:
- estado de carga,
- estado vacio,
- mensajes de error,
- feedback de exito.
2. Definir patron de tabla + acciones + formulario comun para empleados/departamentos.
3. Aplicar lineamientos de accesibilidad minima (foco visible, labels, feedback de validacion).
4. Garantizar comportamiento responsive en desktop y mobile.

### Phase 6 - Error & State Management Strategy

1. Normalizar catalogo de errores UI por codigo HTTP (`400`, `401`, `403`, `404`, `409`, `5xx`).
2. Definir comportamiento consistente de estados por vista:
- loading inicial y en acciones,
- error recuperable con boton de reintento,
- empty state con mensaje y CTA.
3. Evitar estados ambiguos durante transiciones de ruta/autenticacion.
4. Registrar decisiones de UX para conflictos backend (ej. delete no permitido por asociaciones).

### Phase 7 - Cypress Testing Strategy

1. Configurar Cypress para ejecutar contra frontend local con backend en ejecucion.
2. Definir fixtures/datos de prueba basados en usuario demo existente.
3. Implementar E2E minimos obligatorios:
- login exitoso,
- login fallido,
- navegacion a empleados y departamentos,
- flujo CRUD representativo de empleados,
- flujo CRUD representativo de departamentos,
- manejo visible de al menos un conflicto (`409`) o restriccion (`403`).
4. Definir convencion de selectors estables para pruebas (`data-testid` o equivalente).
5. Documentar ejecucion local de Cypress en quickstart.

### Phase 8 - Integration Hardening & Documentation

1. Validar integracion end-to-end con backend real (sin mock como fuente principal de verdad).
2. Confirmar que no hubo cambios innecesarios en backend para cerrar iteracion.
3. Actualizar documentacion de uso frontend (arranque, variables, pruebas).
4. Verificar coherencia final con spec y constitution antes de pasar a tasks.

## Deliverables

1. Estructura frontend Angular 22 LTS en `frontend-angular/`.
2. Sistema de estilos base con Tailwind CSS.
3. Capa de autenticacion y sesion JWT (`AuthService`, guard, interceptor, logout).
4. Modulos/vistas de login, dashboard, empleados y departamentos.
5. Servicios de dominio separados (`auth`, `empleados`, `departamentos`).
6. Patron UI compartido para listados/formularios y estados UX.
7. Suite Cypress E2E base para flujos criticos.
8. Documentacion tecnica/operativa de frontend en artefactos de 004.

## Risks & Mitigations

1. **Riesgo**: divergencia entre payloads esperados en UI y contratos reales backend.
- **Mitigacion**: validacion temprana de contratos en Phase 0 + pruebas E2E sobre backend real.

2. **Riesgo**: problemas de sesion por expiracion de JWT y estados de UI inconsistentes.
- **Mitigacion**: interceptor global de `401`, guard de rutas y estrategia unica de limpieza/redireccion.

3. **Riesgo**: deuda visual por duplicacion de componentes entre empleados/departamentos.
- **Mitigacion**: patron compartido y componentes reutilizables en `shared`.

4. **Riesgo**: fragilidad de pruebas E2E por selectores inestables.
- **Mitigacion**: convencion de selectores dedicados para Cypress desde el inicio.

5. **Riesgo**: necesidad de cambios backend no previstos.
- **Mitigacion**: limitar cambios a incompatibilidades contractuales demostrables y documentadas.

## Out of Scope

- Regenerar backend, modificar arquitectura backend o reescribir endpoints existentes.
- Introducir RBAC avanzado o multiples roles frontend en iteracion 004.
- Cambios funcionales fuera de login + CRUD empleados/departamentos.
- Reemplazar JWT por otro mecanismo de autenticacion.

## Complexity Tracking

No se requiere complejidad adicional fuera del alcance incremental de frontend 004; cualquier desviacion se documentara en `tasks.md` con justificacion explicita.
