# Feature Specification: Iteracion 006 - GitHub Actions y Base de Calidad CI

**Feature Branch**: `006-github-actions-sonarqube`  
**Created**: 2026-04-20  
**Status**: Draft  
**Input**: User description: "Implementar una nueva iteracion de integracion continua con GitHub Actions para validacion automatica de backend y frontend, de forma incremental y sin despliegue."

## Clarifications

### Session 2026-04-20

- Esta iteracion se implementa de forma incremental sobre el proyecto existente.
- El workflow MUST llamarse `CI 01`.
- El workflow MUST ubicarse en `.github/workflows/ci-01.yml`.
- El objetivo de esta iteracion es validacion automatica (build/pruebas), no despliegue.
- La automatizacion MUST incluir jobs separados para backend y frontend.
- El backend MUST validarse con Java 17 y Maven Wrapper.
- El frontend MUST validarse con Node 20 y npm.
- El workflow MUST ejecutarse por `push` y `pull_request` en `master` y `develop`, y por `workflow_dispatch`.
- La iteracion MUST NOT regenerar backend ni frontend, ni alterar arquitectura funcional existente.
- Nota de alcance: aunque el nombre de la iteracion incluye SonarQube, el alcance inicial solicitado en esta especificacion se centra en CI 01 de validacion; cualquier etapa SonarQube se considera expansion posterior salvo requerimiento explicito adicional.

### Session 2026-04-20 (Clarify)

- **DEC-001 (ruta frontend real)**: la carpeta real del frontend en este repositorio es `frontend-angular/` y no `frontend/`. El `working-directory` y `cache-dependency-path` del workflow MUST alinearse a esa ruta real.
- **DEC-002 (Maven Wrapper actual)**: actualmente no existe `mvnw` en la raiz del repositorio ni dentro de `DSW02-Practica01/`. Por lo tanto, `./mvnw -B test` NO es ejecutable en el estado actual sin incorporar previamente Maven Wrapper.
- **DEC-003 (test runner frontend actual)**: el frontend usa `ng test` con builder `@angular/build:unit-test` (stack Vitest). El comando con `--browsers=ChromeHeadless` requiere paquetes browser de Vitest que hoy no estan instalados.
- **DEC-004 (dependencias adicionales para CI frontend)**: para ejecutar pruebas con opcion `--browsers=ChromeHeadless` se requieren dependencias adicionales de Vitest browser (por ejemplo `@vitest/browser-playwright` y motor asociado), y configuracion compatible en pipeline.
- **DEC-005 (nombres de ramas)**: en el repositorio actual se verifica rama `main`; no se observan ramas locales `master` ni `develop`. Los triggers de CI para `master/develop` quedan como supuesto pendiente de confirmacion organizacional antes de implementar.
- **DEC-006 (ambiguedad minima previa a implementacion)**: antes de implementar el workflow CI 01 debe cerrarse la decision entre: (a) introducir Maven Wrapper en backend para cumplir lineamiento, o (b) ajustar el job backend para usar `mvn` sobre `DSW02-Practica01/pom.xml` mientras no exista wrapper.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Ejecucion automatica de CI en ramas objetivo (Priority: P1)

Como equipo de desarrollo, queremos que las validaciones se ejecuten automaticamente en ramas y PR clave para detectar fallos temprano.

**Why this priority**: Sin ejecucion automatica en push/PR no existe control de calidad continuo sobre cambios.

**Independent Test**: Se crea o actualiza una rama de trabajo y se valida que el workflow `CI 01` se dispare correctamente en `push`, `pull_request` y manualmente por `workflow_dispatch`.

**Acceptance Scenarios**:

1. **Given** un `push` a `master` o `develop`, **When** GitHub procesa el evento, **Then** se ejecuta el workflow `CI 01`.
2. **Given** un `pull_request` hacia `master` o `develop`, **When** se crea o actualiza el PR, **Then** se ejecuta el workflow `CI 01`.
3. **Given** un mantenedor con permisos, **When** ejecuta `workflow_dispatch`, **Then** se inicia una corrida manual del workflow.

---

### User Story 2 - Validacion automatica del backend (Priority: P1)

Como equipo backend, queremos un job dedicado que valide pruebas del backend en cada corrida de CI.

**Why this priority**: El backend es componente critico y requiere feedback temprano y aislado.

**Independent Test**: Se valida que el job `test-backend` ejecute checkout, setup de Java 17 con cache Maven y `./mvnw -B test` de forma reproducible.

**Acceptance Scenarios**:

1. **Given** una corrida activa de `CI 01`, **When** inicia `test-backend`, **Then** usa `ubuntu-latest`.
2. **Given** el repositorio clonado en el runner, **When** se configura Java, **Then** se usa temurin 17 con cache de Maven.
3. **Given** permisos de ejecucion de wrapper, **When** se ejecuta `chmod +x mvnw` y `./mvnw -B test`, **Then** el job termina en exito o fallo segun pruebas.

---

### User Story 3 - Validacion automatica del frontend (Priority: P1)

Como equipo frontend, queremos un job dedicado que valide pruebas del frontend en cada corrida de CI.

**Why this priority**: Permite detectar regresiones de UI y tooling sin depender del job backend.

**Independent Test**: Se valida que `test-frontend` ejecute checkout, setup de Node 20 con cache npm y comandos de test en su directorio configurado.

**Acceptance Scenarios**:

1. **Given** una corrida activa de `CI 01`, **When** inicia `test-frontend`, **Then** usa `ubuntu-latest`.
2. **Given** el entorno Node configurado, **When** se aplica cache npm con `cache-dependency-path` del frontend, **Then** la instalacion usa dependencias consistentes.
3. **Given** el directorio de trabajo del frontend, **When** se ejecuta `npm ci` y `npm run test -- --watch=false --browsers=ChromeHeadless --no-progress`, **Then** el job termina en exito o fallo segun resultados.

---

### User Story 4 - Integracion incremental sin despliegue (Priority: P2)

Como responsable tecnico, quiero incorporar CI sin introducir cambios de despliegue ni alteraciones estructurales del sistema.

**Why this priority**: Limita riesgo, mantiene foco y evita acoplar CI con procesos de release no solicitados.

**Independent Test**: Se revisa el workflow y su ejecucion para confirmar que solo valida backend/frontend, sin etapas de deploy/publicacion.

**Acceptance Scenarios**:

1. **Given** el archivo `.github/workflows/ci-01.yml`, **When** se inspecciona su contenido, **Then** contiene solo validaciones de pruebas/build del backend y frontend.
2. **Given** corridas completadas de `CI 01`, **When** se revisan jobs ejecutados, **Then** no existen pasos de despliegue ni publicacion.

---

### Edge Cases

- Si el wrapper Maven no tiene permisos de ejecucion iniciales, el job backend debe corregirlo con `chmod +x mvnw`.
- Si hay error de cache (Maven/npm), el workflow debe seguir pudiendo ejecutar instalacion/compilacion limpia.
- Si una capa falla (backend o frontend), el workflow debe marcar estado fallido y exponer logs del job correspondiente.
- Si se corre manualmente (`workflow_dispatch`) sin cambios recientes, la corrida debe seguir siendo valida y reproducible.
- Si cambia la ruta del frontend en el repositorio, el workflow debe actualizar `working-directory` y `cache-dependency-path` de forma consistente.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El repositorio MUST incluir un workflow llamado `CI 01`.
- **FR-002**: El workflow MUST definirse en `.github/workflows/ci-01.yml`.
- **FR-003**: El workflow MUST activarse por `push` en ramas `master` y `develop`.
- **FR-004**: El workflow MUST activarse por `pull_request` hacia ramas `master` y `develop`.
- **FR-005**: El workflow MUST soportar ejecucion manual por `workflow_dispatch`.
- **FR-006**: El workflow MUST incluir un job `test-backend` separado del job frontend.
- **FR-007**: `test-backend` MUST ejecutarse sobre `ubuntu-latest`.
- **FR-008**: `test-backend` MUST ejecutar `actions/checkout`.
- **FR-009**: `test-backend` MUST configurar Java temurin 17 con cache Maven.
- **FR-010**: `test-backend` MUST ejecutar `chmod +x mvnw`.
- **FR-011**: `test-backend` MUST ejecutar `./mvnw -B test`.
- **FR-012**: El workflow MUST incluir un job `test-frontend` separado del backend.
- **FR-013**: `test-frontend` MUST ejecutarse sobre `ubuntu-latest`.
- **FR-014**: `test-frontend` MUST ejecutar `actions/checkout`.
- **FR-015**: `test-frontend` MUST configurar Node 20 con cache npm.
- **FR-016**: `test-frontend` MUST definir `cache-dependency-path` apuntando al `package-lock.json` del frontend.
- **FR-017**: `test-frontend` MUST usar un `working-directory` de frontend consistente con la estructura del repositorio.
- **FR-018**: `test-frontend` MUST ejecutar `npm ci`.
- **FR-019**: `test-frontend` MUST ejecutar `npm run test -- --watch=false --browsers=ChromeHeadless --no-progress`.
- **FR-020**: La iteracion MUST enfocarse en validacion automatica y MUST NOT incluir despliegue.
- **FR-021**: La iteracion MUST NOT regenerar backend/frontend ni alterar arquitectura funcional existente.
- **FR-022**: La automatizacion MUST integrarse en el mismo proyecto y respetar su estructura de carpetas actual.

### Key Entities *(include if feature involves data)*

- **CIWorkflowDefinition**: Definicion YAML del workflow `CI 01` con triggers, jobs y pasos.
- **BackendValidationJob**: Job aislado para validacion de backend con Java 17 + Maven Wrapper.
- **FrontendValidationJob**: Job aislado para validacion de frontend con Node 20 + npm.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de eventos `push` a `master` y `develop` dispara el workflow `CI 01`.
- **SC-002**: El 100% de `pull_request` hacia `master` y `develop` dispara el workflow `CI 01`.
- **SC-003**: El workflow puede ejecutarse manualmente por `workflow_dispatch` en cualquier momento.
- **SC-004**: El job `test-backend` ejecuta exitosamente `./mvnw -B test` cuando el backend esta sano.
- **SC-005**: El job `test-frontend` ejecuta exitosamente `npm ci` y `npm run test -- --watch=false --browsers=ChromeHeadless --no-progress` cuando el frontend esta sano.
- **SC-006**: Una falla en backend o frontend se refleja como corrida fallida con trazabilidad por job.
- **SC-007**: No se introducen etapas de despliegue en `CI 01`.
- **SC-008**: La iteracion 006 se incorpora sin regenerar componentes existentes ni alterar arquitectura funcional.
