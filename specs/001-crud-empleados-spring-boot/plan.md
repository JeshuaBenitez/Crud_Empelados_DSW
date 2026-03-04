# Implementation Plan: CRUD de Empleados

**Branch**: `001-crud-empleados-spring-boot` | **Date**: 2026-02-26 | **Spec**: `/specs/001-crud-empleados-spring-boot/spec.md`
**Input**: Feature specification from `/specs/001-crud-empleados-spring-boot/spec.md`

## Summary

Implementar una API backend con Spring Boot 3 y Java 17 para CRUD de empleados con validaciones de campos, persistencia en PostgreSQL, autenticación HTTP Basic y documentación Swagger/OpenAPI. La solución usa una PK compuesta (`prefijo`, `numero`) y expone una clave lógica derivada con formato `EMP-<numero>`, manteniendo arquitectura por capas, migraciones Flyway y entorno local Docker para base de datos.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Security, Data JPA), Flyway, PostgreSQL Driver, Springdoc OpenAPI  
**Storage**: PostgreSQL 16 (Docker en local)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc  
**Target Platform**: Linux server / JVM 17  
**Project Type**: Backend web-service monolítico  
**Performance Goals**: Operaciones CRUD con tiempo de respuesta p95 < 300 ms en entorno local de desarrollo  
**Constraints**: Basic Auth obligatoria en `/api/empleados/**`, longitud máxima 100 para `nombre`, `direccion`, `telefono`, PK compuesta (`prefijo`, `numero`), generación automática de clave lógica `EMP-<numero>`, duplicados de PK compuesta con `409 Conflict`  
**Scale/Scope**: MVP de un módulo de empleados para operación interna (cientos a miles de registros)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Runtime Base Inmutable**: PASS. Se usa Spring Boot 3 y Java 17 como stack único.
- **II. Seguridad de Acceso Mínimo**: PASS. Endpoints de negocio protegidos por HTTP Basic con Spring Security.
- **III. Persistencia PostgreSQL Containerizada**: PASS. Base de datos objetivo PostgreSQL y ejecución local por Docker Compose.
- **IV. Contrato API en Swagger**: PASS. Se publicará OpenAPI y Swagger UI con operaciones y errores esperados.
- **V. Calidad Verificable y Trazabilidad**: PASS. Se definen pruebas unitarias/integración y artefactos de diseño versionados.

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
├── docker-compose.yml
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/dwgabo/dsw02practica01/
    │   │   ├── config/
    │   │   ├── controller/
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

**Structure Decision**: Se adopta un backend monolítico Spring Boot en `DSW02-Practica01/` con separación por capas y documentación funcional en `specs/001-crud-empleados-spring-boot/`.

## Phase 0 - Research

1. Confirmar estrategia de generación de clave lógica `EMP-<numero>` basada en PK compuesta y consecutivo autonumérico.
2. Confirmar estrategia de manejo de errores REST para duplicados (`409 Conflict`) y no encontrados (`404`).
3. Confirmar política de rechazo de clave manual enviada por cliente en operaciones de alta.
4. Definir postura de seguridad para Swagger UI con endpoints documentados y autenticación para pruebas.
5. Verificar compatibilidad de versiones Spring Boot 3, Java 17, PostgreSQL 16 y springdoc.

## Phase 1 - Design

1. Diseñar entidad `Empleado` con PK compuesta y clave lógica derivada.
2. Diseñar contrato API CRUD y códigos de respuesta con clave `EMP-<numero>`.
3. Diseñar migración inicial Flyway para tabla `empleados` con PK compuesta (`prefijo`, `numero`) y secuencia autonumérica.
4. Definir quickstart reproducible con Docker + ejecución de aplicación.

## Phase 2 - Task Planning Approach

1. Crear tareas de infraestructura (dependencias, configuración, Docker, migraciones).
2. Crear tareas por historia (P1, P2, P3) con pruebas asociadas.
3. Añadir tareas de verificación de Swagger, seguridad y casos borde críticos.
4. Incluir checklist final de build + pruebas + validación manual mínima.

## Complexity Tracking

No se identifican violaciones constitucionales ni complejidad adicional que requiera justificación.
