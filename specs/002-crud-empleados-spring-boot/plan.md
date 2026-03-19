# Implementation Plan: Autenticacion de Empleados por Correo y Password

**Branch**: `002-crud-empleados-spring-boot` | **Date**: 2026-03-12 | **Spec**: `/specs/002-crud-empleados-spring-boot/spec.md`
**Input**: Feature specification from `/specs/002-crud-empleados-spring-boot/spec.md`

## Summary

Extender el backend existente para habilitar autenticacion de empleados por `correo` y `password` persistidos en PostgreSQL, con emision de token JWT para acceso `JWT only` a `/api/v1/empleados/**`, manteniendo Basic Auth solo para rutas administrativas, Swagger/OpenAPI usable y operacion local en Docker + PostgreSQL. La iteracion incluye script de carga obligatoria previa, seed inicial de autenticacion y alcance de autorizacion limitado a recurso propio, sin incluir departamentos.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Security, Validation, Data JPA), Flyway, PostgreSQL Driver, Springdoc OpenAPI, libreria JWT (a definir en research)  
**Storage**: PostgreSQL 16 (Docker Compose)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc  
**Target Platform**: Linux server / JVM 17  
**Project Type**: Backend web-service monolitico  
**Performance Goals**: N/A (sin objetivo cuantitativo adicional en 002)  
**Constraints**: Login `POST /api/v1/auth/empleados/login`, hash de password obligatorio, JWT only en `/api/v1/empleados/**`, Basic solo admin, script de carga obligatoria previa, seed inicial para pruebas, Swagger publico, sin alcance de departamentos  
**Scale/Scope**: Ajuste incremental sobre API existente de empleados

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Runtime Base Inmutable**: PASS. Se mantiene Spring Boot 3 + Java 17 sin cambio de stack.
- **II. Seguridad Evolutiva por Iteraciones**: PASS. Iteracion 002 implementa autenticacion por correo/password persistido, hash obligatorio, JWT para negocio de empleados y Basic reservado a administracion.
- **III. Persistencia PostgreSQL Containerizada**: PASS. Validacion de credenciales contra PostgreSQL con operacion en Docker Compose.
- **IV. Contrato API Versionado en Swagger**: PASS. Se documenta endpoint versionado de login y esquema Bearer JWT en OpenAPI.
- **V. Calidad Verificable, Paginacion y Trazabilidad**: PASS. Se planifican pruebas de login exitoso/fallido, autorizacion por token y no-regresion de endpoints existentes.
- **Alcance por Iteracion**: PASS. Se limita a autenticacion de empleados; departamentos quedan fuera para iteracion 003.

## Project Structure

### Documentation (this feature)

```text
specs/002-crud-empleados-spring-boot/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── auth-empleados-openapi.yaml
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
    │   │   ├── dto/
    │   │   ├── exception/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── security/          # nuevo paquete esperado para JWT
    │   │   ├── service/
    │   │   └── Dsw02Practica01Application.java
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    └── test/java/com/dwgabo/dsw02practica01/
        ├── controller/
        ├── security/
        └── service/
```

**Structure Decision**: Se conserva la estructura actual y se agregan componentes de autenticacion JWT de forma incremental (sin regenerar proyecto ni cambiar arquitectura base).

## Phase 0 - Research

1. Seleccionar libreria JWT compatible con Spring Boot 3 y Java 17 (firma, expiracion, validacion).
2. Definir estrategia de convivencia sin bypass: Basic solo admin y JWT only en rutas de empleados.
3. Confirmar politica de password hash (BCrypt), script de carga obligatoria previa y seed inicial para pruebas.
4. Definir estrategia de errores de login (`400`, `401`) sin filtrado de existencia de correo.
5. Validar lineamientos de OpenAPI para documentar login y esquema Bearer JWT manteniendo Swagger publico.

## Phase 1 - Design

1. Diseñar modelo de autenticacion de empleado (correo unico, passwordHash, estado de cuenta) y marca de identidad para autorizacion sobre recurso propio.
2. Diseñar contrato de endpoint `POST /api/v1/auth/empleados/login` (request/response y errores).
3. Diseñar reglas de proteccion de endpoints:
   - Publicos: Swagger + login.
   - Protegidos por JWT only: `/api/v1/empleados/**`.
   - Administrativos con Basic: rutas de administracion.
4. Diseñar migracion de base de datos para soportar correo unico y password hash en empleados, incluyendo estrategia de script de carga obligatoria previa.
5. Diseñar autorizacion de empleado sobre su propio recurso y respuesta `403` en recursos de terceros.
6. Diseñar quickstart de autenticacion (flujo login, uso de token, seed inicial y prueba en Docker).

## Phase 2 - Task Planning Approach

1. Crear tareas de persistencia y modelo de autenticacion (entidad/repo/migracion + script de carga previa + seed).
2. Crear tareas de seguridad (servicio de autenticacion, generacion/validacion JWT, filtros y reglas de acceso).
3. Crear tareas de endpoint login y DTOs asociados.
4. Crear tareas de integracion con endpoints existentes para requerir Bearer JWT only en empleados y Basic solo en administracion.
5. Crear tareas de OpenAPI/Swagger y quickstart para pruebas manuales.
6. Crear tareas de pruebas unitarias e integracion:
   - login exitoso/fallido,
   - token valido/invalido/expirado,
   - autorizacion de recurso propio (`403` para terceros),
   - no-regresion de arranque con Docker + PostgreSQL,
   - no inclusion de departamentos.

## Complexity Tracking

No se requiere complejidad adicional fuera del alcance incremental definido para la iteracion 002.
