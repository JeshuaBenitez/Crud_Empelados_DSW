# Implementation Plan: CRUD de Departamentos y Relacion con Empleados

**Branch**: `003-crud-departamentos-spring-boot` | **Date**: 2026-03-19 | **Spec**: `/specs/003-crud-departamentos-spring-boot/spec.md`  
**Input**: Feature specification from `/specs/003-crud-departamentos-spring-boot/spec.md`

## Summary

Extender incrementalmente el backend existente para incorporar `Departamento` con CRUD versionado en `/api/v1/departamentos`, relacionarlo con `Empleado` (1 departamento por empleado, N empleados por departamento), hacer obligatoria la relacion al cierre de iteracion, imponer unicidad case-insensitive de nombre, bloquear eliminacion con empleados asociados (`409 Conflict`), proteger endpoints de departamentos con JWT, y mantener compatibilidad con OpenAPI/Swagger, PostgreSQL y Docker sin regenerar el proyecto.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Security, Validation, Data JPA), Flyway, PostgreSQL Driver, Springdoc OpenAPI  
**Storage**: PostgreSQL 16 (Docker Compose)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc (+ pruebas de repositorio/integracion donde aplique)  
**Target Platform**: Linux server / JVM 17  
**Project Type**: Backend web-service monolitico incremental  
**Performance Goals**: N/A (iteracion funcional, sin objetivo cuantitativo adicional)  
**Constraints**:
- Relacion final no-null de empleado a departamento
- `nombre` de departamento unico case-insensitive
- `DELETE` con empleados asociados retorna `409 Conflict`
- Endpoints `/api/v1/departamentos/**` protegidos por JWT
- DTOs para evitar ciclos de serializacion
- Clave logica departamento `DEP-<numero>`
- Listado paginado por defecto (`page=0`, `size=10`, `size<=100`)
- Sin regenerar proyecto ni romper flujos de iteracion 002  
**Scale/Scope**: Ajuste incremental de dominio, persistencia, API y pruebas sobre estructura actual

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **I. Runtime Base Inmutable**: PASS. Se mantiene Spring Boot 3 + Java 17.
- **II. Seguridad Evolutiva y Compatibilidad por Iteraciones**: PASS. Departamentos se integran con JWT y sin romper reglas de seguridad vigentes.
- **III. Persistencia PostgreSQL Containerizada**: PASS. Cambios por migraciones incrementales en PostgreSQL, compatibles con Docker Compose.
- **IV. Contrato API Versionado en Swagger**: PASS. CRUD de departamentos y errores de negocio documentados en OpenAPI.
- **V. Calidad Verificable, Paginacion y Trazabilidad**: PASS. Se planifican pruebas de CRUD, integridad referencial, seguridad y regresion.
- **Reglas de Evolucion del Dominio (Iteración 003)**: PASS. Se agrega nuevo agregado y relacion sin refactor destructivo ni alteracion retrospectiva de migraciones previas.

## Project Structure

### Documentation (this feature)

```text
specs/003-crud-departamentos-spring-boot/
├── plan.md
├── spec.md
├── quickstart.md            # pendiente en fase de documentacion
├── contracts/               # pendiente de contrato openapi de departamentos
└── tasks.md                 # pendiente en /speckit.tasks
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
    │   │   ├── security/
    │   │   ├── service/
    │   │   └── Dsw02Practica01Application.java
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    └── test/java/com/dwgabo/dsw02practica01/
```

**Structure Decision**: Se conserva estructura actual y se agrega modulo de Departamentos de forma incremental con impacto acotado en Empleados.

## Implementation Strategy

### Phase 0 - Research & Design Decisions

1. Definir estrategia de migracion en dos pasos para obligatoriedad final de departamento en empleado:
- Paso transitorio para poblar/normalizar datos existentes.
- Paso final para constraint no-null e integridad referencial.
2. Definir comparacion case-insensitive para unicidad de `nombre` de departamento a nivel DB y servicio.
3. Confirmar politica de borrado restringido de departamento (`409` cuando existan empleados asociados), sin cascada destructiva.
4. Definir contrato de clave logica `DEP-<numero>` y estrategia de generacion de consecutivo.
5. Definir diseño DTO para evitar ciclos JSON en respuestas de Empleado/Departamento.

### Phase 1 - Database Migrations (Flyway)

1. Crear migracion para tabla `departamentos` con PK compuesta equivalente al patrón de claves logicas y campos base.
2. Crear soporte de consecutivo para clave `DEP-<numero>` (secuencia o mecanismo equivalente consistente con convencion del proyecto).
3. Crear migracion para relacionar `empleados` con `departamentos` mediante FK.
4. Ejecutar etapa de migracion de datos para asignacion inicial de departamento a empleados existentes (estrategia de transicion definida en spec).
5. Aplicar restriccion final no-null en FK de empleado hacia departamento.
6. Crear indice/constraint para unicidad case-insensitive de `nombre` de departamento.
7. Validar orden de migraciones y no-modificacion de migraciones historicas 001/002.

### Phase 2 - Domain Model & Repository

1. Incorporar entidad `Departamento` en capa de modelo con campos de dominio y clave logica.
2. Extender entidad `Empleado` con referencia obligatoria a `Departamento` (estado final).
3. Ajustar mapeos JPA de relacion 1:N / N:1 cuidando fetch y evitando serializacion directa de entidades.
4. Crear/actualizar repositorios:
- `DepartamentoRepository` con busquedas por clave y validacion de nombre unico case-insensitive.
- Consultas para verificar existencia de empleados por departamento antes de eliminar.
5. Definir consultas de paginacion para listado de departamentos.

### Phase 3 - Application Layer (Service)

1. Implementar `DepartamentoService` con operaciones CRUD.
2. Aplicar validaciones de negocio:
- nombre requerido y unico (case-insensitive),
- formato de clave,
- conflictos por nombre duplicado,
- bloqueo de borrado con empleados asociados (`409`).
3. Integrar reglas de consistencia para relacion con empleados.
4. Alinear manejo de excepciones con `GlobalExceptionHandler` para respuestas (`400`, `404`, `409`).

### Phase 4 - API Layer (Controller + DTO/Serialization)

1. Crear controlador versionado `DepartamentoController` en `/api/v1/departamentos`.
2. Definir DTOs de request/response de departamentos.
3. Ajustar DTOs de empleados para incluir referencia plana de departamento (`departamentoClave`, `departamentoNombre`) sin grafo recursivo.
4. Implementar paginacion en `GET /api/v1/departamentos` con defaults y validacion de limites.
5. Asegurar contratos HTTP esperados (`201`, `200`, `204`, `400`, `404`, `409`, `401/403` segun seguridad/reglas).

### Phase 5 - Security (JWT Impact)

1. Incorporar `/api/v1/departamentos/**` en la cadena de seguridad JWT de negocio.
2. Confirmar que no se habilita bypass por Basic en rutas de departamentos.
3. Mantener compatibilidad con reglas administrativas actuales y rutas publicas ya definidas.
4. Agregar pruebas de acceso con token valido/invalido/sin token para departamentos.

### Phase 6 - OpenAPI/Swagger Impact

1. Documentar CRUD de departamentos en OpenAPI con rutas versionadas.
2. Documentar esquema de seguridad Bearer JWT para endpoints de departamentos.
3. Documentar errores de negocio (`409` por nombre duplicado y por borrado con empleados asociados).
4. Documentar modelo de respuesta sin ciclos (DTOs resumidos).
5. Alinear quickstart de pruebas manuales (login + operaciones departamentos).

### Phase 7 - Docker/PostgreSQL Compatibility

1. Verificar que Docker Compose actual no requiere regeneracion ni cambios disruptivos.
2. Validar arranque de app con migraciones 003 aplicadas sobre base existente.
3. Confirmar compatibilidad de variables de entorno y puertos.
4. Ejecutar smoke test de endpoints de departamentos contra stack dockerizado.

### Phase 8 - Test Plan (Required)

1. **Unit tests**
- Servicio de departamentos: crear, actualizar, eliminar, validaciones y conflictos.
- Regla de nombre unico case-insensitive.
- Regla de bloqueo de eliminacion con empleados asociados.
2. **Integration tests (API)**
- CRUD de departamentos autenticado con JWT.
- Paginacion (`page/size` defaults y limites).
- `404` por clave inexistente.
- `409` por duplicado de nombre y por delete con asociaciones.
3. **Security tests**
- `401` sin token / token invalido en `/api/v1/departamentos/**`.
- Confirmar no acceso por mecanismo no permitido en rutas de negocio.
4. **Persistence/Migration tests**
- Aplicacion de migraciones 003 en base con datos de 002.
- Integridad referencial y no-null final en empleado.departamento.
5. **Regression tests**
- Flujos de autenticacion y endpoints de empleados existentes no se degradan.
- Arranque general de aplicacion y Swagger permanece operativo.

## Deliverables

1. Migraciones Flyway de Departamentos y relacion con Empleados.
2. Modelo JPA actualizado (`Departamento` + relacion en `Empleado`).
3. Repositorio, servicio y controlador de departamentos.
4. DTOs y respuestas ajustadas para evitar ciclos de serializacion.
5. Configuracion de seguridad JWT actualizada para departamentos.
6. OpenAPI/Swagger y quickstart actualizados.
7. Suite de pruebas (unitarias, integracion, seguridad y regresion) en verde.

## Risks & Mitigations

1. **Riesgo**: migracion de empleados existentes sin departamento.
- **Mitigacion**: estrategia transitoria de poblado + constraint final no-null en migracion posterior.
2. **Riesgo**: ciclos JSON por relacion bidireccional.
- **Mitigacion**: DTOs de salida desacoplados de entidades y vistas resumidas.
3. **Riesgo**: bloqueo de operaciones por restricciones no contempladas.
- **Mitigacion**: mapear conflictos de negocio a `409` con mensajes consistentes.
4. **Riesgo**: regresion de seguridad en endpoints existentes.
- **Mitigacion**: pruebas de seguridad y regresion antes de cierre.

## Out of Scope

- Regenerar el proyecto o cambiar arquitectura base.
- Replantear el mecanismo de autenticacion de iteracion 002 fuera de los ajustes estrictos de integracion.
- Funcionalidades avanzadas de departamentos fuera de CRUD + relacion definida en spec.

## Complexity Tracking

No se requiere complejidad adicional fuera del alcance incremental definido para la iteracion 003; cualquier desviacion se documentara en `tasks.md` con justificacion explicita.
