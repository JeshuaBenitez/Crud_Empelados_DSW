# Implementation Plan: CRUD de Empleados (API v1)

**Branch**: `001-crud-empleados-spring-boot` | **Date**: 2026-03-09 | **Spec**: `/specs/001-crud-empleados-spring-boot/spec.md`
**Input**: Feature specification from `/specs/001-crud-empleados-spring-boot/spec.md`

## Summary

Evolucionar el backend existente de empleados para exponer CRUD versionado en `/api/v1/empleados`, mantener clave lógica `EMP-<numero>` con PK compuesta, aplicar HTTP Basic con credenciales fijas `admin/admin123`, agregar paginación obligatoria en listado (`page=0`, `size=10`, `max=100`), mantener compatibilidad con Swagger/OpenAPI y conservar operación con Docker + PostgreSQL sin reconstruir el proyecto desde cero.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Security, Data JPA), Flyway, PostgreSQL Driver, Springdoc OpenAPI  
**Storage**: PostgreSQL 16 (Docker/Compose)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc  
**Target Platform**: Linux server / JVM 17  
**Project Type**: Backend web-service monolítico  
**Performance Goals**: N/A (no objetivo cuantitativo adicional en esta iteración)  
**Constraints**: `/api/v1/empleados/**`, Basic Auth fija `admin/admin123`, paginación (`page=0`, `size=10`, `size<=100`), Swagger actualizado, Docker + PostgreSQL operativos  
**Scale/Scope**: Ajuste incremental sobre módulo CRUD existente

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Runtime Base Inmutable**: PASS. Se mantiene Spring Boot 3 + Java 17 sin cambio de stack.
- **II. Seguridad Básica Académica**: PASS. Se define HTTP Basic con credenciales fijas `admin/admin123`.
- **III. Persistencia PostgreSQL Containerizada**: PASS. Se mantiene PostgreSQL con Docker Compose y compatibilidad local.
- **IV. Contrato API Versionado en Swagger**: PASS. Se planifica actualización de endpoints a `/api/v1/...` y reflejo en OpenAPI.
- **V. Calidad Verificable, Paginación y Trazabilidad**: PASS. Se incorporan pruebas para paginación, autenticación y contrato versionado.

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleados-spring-boot/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados-openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
DSW02-Practica01/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/dwgabo/dsw02practica01/
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── Dsw02Practica01Application.java
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    └── test/java/com/dwgabo/dsw02practica01/
```

**Structure Decision**: Se conserva la estructura actual y se aplican cambios incrementales de contrato/API sin reinicio de proyecto.

## Phase 0 - Research

1. Validar estrategia de versionado uniforme (`/api/v1/empleados`) para todos los endpoints CRUD.
2. Confirmar política de autenticación fija `admin/admin123` en configuración de seguridad y pruebas.
3. Definir comportamiento de paginación en listado: defaults y límites de validación.
4. Confirmar impacto en documentación OpenAPI y ejemplos de quickstart.
5. Verificar que dockerización de API + PostgreSQL no se rompe con los nuevos cambios.

## Phase 1 - Design

1. Ajustar contrato API para rutas versionadas y query params `page`/`size`.
2. Diseñar respuesta de listado paginado y validaciones (`size <= 100`).
3. Ajustar configuración de seguridad para credenciales académicas fijas.
4. Actualizar documentación Swagger/OpenAPI y guía de ejecución Docker.

## Phase 2 - Task Planning Approach

1. Crear tareas de refactor de rutas en controller/service y pruebas asociadas.
2. Crear tareas de paginación en endpoint GET de listado y validación de parámetros.
3. Crear tareas de alineación de seguridad (`admin/admin123`) y pruebas de acceso.
4. Crear tareas de actualización Swagger, quickstart y verificación Docker + PostgreSQL.
5. Incluir verificación final de build, pruebas y smoke test en contenedores.

## Complexity Tracking

No se requiere complejidad adicional fuera del alcance incremental definido.
