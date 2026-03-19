<!--
Sync Impact Report
- Version change: 2.2.0 -> 2.3.0
- Modified principles:
	- I. Runtime Base Inmutable -> I. Runtime Base Inmutable y Bimodal
	- II. Seguridad Evolutiva y Compatibilidad por Iteraciones (extensión de flujo JWT para frontend)
	- IV. Contrato API Versionado en Swagger (consumo contractual desde frontend)
	- V. Calidad Verificable, Paginación y Trazabilidad (cobertura E2E frontend con Cypress)
- Added sections:
	- Reglas de Evolución Frontend (Iteración 004)
- Removed sections:
	- Ninguna
- Templates requiring updates:
	- ⚠ pendiente de verificación para plantillas que referencian stack único backend
- Deferred TODOs:
	- Sincronizar plantillas tras confirmar alcance final de Iteración 004
-->

# DSW02-Practica01 Constitution

## Core Principles

### I. Runtime Base Inmutable y Bimodal
El backend MUST ejecutarse sobre Spring Boot 3 y Java 17, manteniendo el stack estándar
de Spring (Spring Web, Spring Security, Spring Data JPA cuando aplique). El frontend
MUST implementarse con Angular 22 LTS y MUST residir en `frontend-angular/` como módulo
del mismo repositorio.
Las nuevas funcionalidades MUST respetar esta separación de responsabilidades
(frontend UI + backend API) y MUST evitar duplicar lógica de negocio del backend en el cliente.
Rationale: una base bimodal explícita reduce acoplamiento, preserva mantenibilidad y habilita evolución incremental sin rehacer arquitectura existente.

### II. Seguridad Evolutiva y Compatibilidad por Iteraciones
Todo endpoint no público MUST permanecer protegido por Spring Security y la estrategia
de autenticación MUST evolucionar por iteración funcional.

- En iteración 001, la autenticación básica HTTP con `admin` / `admin123` se admite
	como mecanismo global académico para operación y validación inicial.
- En iteración 002, el sistema MUST incorporar autenticación de empleados basada en
	`correo` y `password` persistidos en PostgreSQL.
- En iteración 003, los nuevos endpoints de departamentos MUST conservar compatibilidad
	con el esquema de autenticación vigente (JWT para negocio de empleados y reglas
	administrativas existentes), salvo enmienda explícita de seguridad.
- En iteración 004, el frontend MUST autenticarse usando el endpoint de login de empleados
	ya existente y MUST consumir recursos protegidos usando el JWT emitido por backend,
	sin introducir mecanismos de autenticación paralelos ni credenciales hardcodeadas.
- La autenticación de empleados MUST validar credenciales contra datos persistidos y
	MUST almacenar contraseñas con hash seguro (nunca texto plano).
- La opción JWT MAY adoptarse en iteración 002 cuando mejore separación entre login de
	empleados y acceso a recursos protegidos.

Rationale: mantener un baseline simple en la primera iteración y evolucionar a un modelo
de identidad de negocio reduce riesgo de entrega y habilita escalabilidad de seguridad.

### III. Persistencia PostgreSQL Containerizada
Toda persistencia relacional MUST usar PostgreSQL. Para desarrollo e integración local,
la base MUST ejecutarse en Docker (Docker Compose u orquestación equivalente) y la
aplicación MUST poder levantar contra esa instancia sin cambios de código. La
configuración de conexión MUST ser parametrizable por entorno.
Toda nueva entidad de dominio (por ejemplo Departamentos en iteración 003) MUST
introducirse mediante migraciones incrementales y relaciones explícitas, sin
recrear tablas existentes ni romper compatibilidad de datos previos.
Rationale: la paridad de entorno reduce incidencias entre desarrollo, pruebas y despliegue.

### IV. Contrato API Versionado en Swagger
Toda API HTTP MUST estar documentada y publicada en Swagger/OpenAPI. Cada endpoint
debe declarar operación, parámetros, respuestas esperadas y códigos de error. El contrato
MUST incluir versionado explícito de rutas (por ejemplo `/api/v1/...`). Ningún cambio
de contrato se considera completo sin actualización de la documentación Swagger y
validación de consistencia con la implementación.
El frontend de iteración 004 MUST consumir exclusivamente endpoints versionados y
documentados, alineando payloads y códigos de respuesta con el contrato OpenAPI vigente.
Rationale: versionar explícitamente evita ruptura de clientes y mejora gobernanza del API.

### V. Calidad Verificable, Paginación y Trazabilidad
Cada historia implementada MUST incluir pruebas automatizadas de unidad y/o integración
proporcionales al riesgo, y MUST registrar decisiones técnicas relevantes en los artefactos
de especificación/plan/tareas. Toda entrega MUST validar arranque de aplicación,
conectividad a PostgreSQL en Docker y disponibilidad del endpoint de Swagger. Todo
endpoint de listado GET MUST soportar paginación explícita y documentada.
Cuando se agreguen relaciones entre agregados (ej. Empleado-Departamento), las
pruebas MUST cubrir integridad referencial, comportamiento CRUD y regresión de
casos existentes de iteraciones previas.
En iteración 004, la capa frontend MUST incorporar pruebas automatizadas con Cypress
para flujos críticos (login, navegación principal y operaciones CRUD clave), en
proporción al riesgo de regresión.
Rationale: sin evidencia verificable y sin control de volumen en listados no existe criterio objetivo de completitud.

## Alcance por Iteracion

- Iteracion 001 (`001-crud-empleados-spring-boot`): CRUD de empleados, API versionada,
	paginacion, PostgreSQL en Docker, Swagger/OpenAPI y Basic Auth global.
- Iteracion 002 (`002-crud-empleados-spring-boot`): extension de seguridad para login
	de empleados por `correo` + `password` con persistencia en base de datos (y JWT opcional).
- Iteracion 003 (`003-*`): CRUD de departamentos y relación con empleados sobre el
	proyecto existente. MUST ejecutarse de forma incremental, sin regenerar el proyecto
	y sin alterar la base funcional de iteración 002 más allá de lo estrictamente
	necesario para modelar la relación.
- Iteracion 004 (`004-frontend-angular`): implementación del frontend en Angular 22 LTS
	en `frontend-angular/`, con especificación en `specs/004-frontend-angular/`, consumo
	de API backend existente, flujo de login de empleados y pantallas para login,
	CRUD de empleados y CRUD de departamentos. La evolución MUST ser incremental,
	sin regenerar backend ni reestructurar innecesariamente el repositorio.

## Restricciones Técnicas Obligatorias

- El proyecto backend MUST estructurarse como servicio web Spring Boot.
- Java MUST fijarse en versión 17 en build y runtime.
- El frontend MUST implementarse con Angular 22 LTS dentro de `frontend-angular/`.
- La especificación de frontend MUST mantenerse en `specs/004-frontend-angular/`.
- El frontend MUST consumir la API existente del backend y MUST respetar rutas versionadas.
- El flujo de autenticación frontend MUST usar `POST /api/v1/auth/empleados/login`.
- El frontend MUST gestionar JWT de forma compatible con la seguridad backend vigente.
- El frontend MUST incluir pantallas de login, CRUD de empleados y CRUD de departamentos.
- El frontend MUST usar Tailwind CSS como base de interfaz.
- La iteración frontend MUST contemplar pruebas E2E con Cypress.
- La autenticación básica MUST aplicarse al menos a rutas de negocio y administración.
- El acceso básico MUST operar con usuario `admin` y contraseña `admin123`.
- La iteración 002 MUST implementar autenticación de empleados por `correo` y `password`
	validados contra datos persistidos en PostgreSQL.
- La iteración 003 MUST crear entidad y tabla de departamentos en PostgreSQL mediante
	migraciones incrementales.
- La iteración 003 MUST relacionar empleados con departamentos preservando integridad
	referencial y compatibilidad de datos existentes.
- La iteración 003 MUST exponer CRUD de departamentos con documentación OpenAPI y
	rutas versionadas.
- Las contraseñas de empleados MUST almacenarse con hash seguro y no en texto plano.
- Si se adopta JWT, el contrato OpenAPI MUST documentar endpoint de autenticación,
	formato del token, expiración y respuestas de error.
- PostgreSQL MUST ser el motor de datos por defecto para cualquier módulo persistente.
- Docker MUST proveer entorno reproducible para base de datos en desarrollo.
- La integración frontend-backend MUST mantener compatibilidad con JWT, Docker y arquitectura existente.
- La evolución de iteración 004 MUST NOT regenerar backend ni alterar su estructura base sin justificación aprobada en especificación.
- Swagger/OpenAPI MUST estar habilitado en ambientes no productivos como mínimo.
- Los endpoints HTTP MUST incluir versión explícita en la ruta (`/v1` o equivalente).
- Los endpoints GET de listado MUST exponer parámetros de paginación.

## Reglas de Evolución del Dominio (Iteración 003)

- La incorporación de Departamentos MUST tratarse como expansión del dominio, no como
	refactor destructivo del modelo actual.
- Toda relación entre Empleado y Departamento MUST declararse explícitamente en modelo,
	esquema y contrato API.
- Las reglas de negocio preexistentes de Empleados MUST mantenerse operativas tras la
	integración con Departamentos, salvo cambios aprobados en especificación de iteración.
- Ninguna migración de Iteración 003 MUST modificar retrospectivamente migraciones ya
	aplicadas de iteraciones 001/002.

## Reglas de Evolución Frontend (Iteración 004)

- La iteración 004 MUST tratarse como expansión del proyecto existente, no como
	reemplazo del backend ni reconstrucción del repositorio.
- Toda funcionalidad de UI MUST mapearse explícitamente a capacidades backend
	existentes antes de proponer nuevos endpoints.
- El frontend MUST centralizar configuración de URL base API por entorno para
	compatibilidad local y containerizada.
- La gestión de sesión JWT en frontend MUST considerar expiración, manejo de 401
	y cierre de sesión coherente con seguridad backend.
- Tailwind CSS MUST aplicarse como sistema base de estilos, preservando consistencia
	visual y mantenibilidad.

## Flujo de Entrega y Puertas de Calidad

1. Toda especificación MUST declarar requisitos de seguridad (Basic Auth), persistencia
	(PostgreSQL) y contrato API (Swagger + versionado explícito).
   En iteración 002 MUST incluir login de empleados por correo/password persistido.
	En iteración 003 MUST incluir relación Empleado-Departamento y CRUD de departamentos.
   En iteración 004 MUST incluir alcance de frontend Angular 22, consumo API existente,
	flujo JWT de empleados, uso de Tailwind y estrategia de pruebas Cypress.
2. Todo plan MUST incluir un Constitution Check explícito para stack, seguridad,
	base de datos containerizada y documentación API.
3. Toda lista de tareas MUST contemplar configuración Docker/PostgreSQL, seguridad,
	documentación Swagger, versionado de endpoints, paginación y pruebas.
   En iteración 002 MUST contemplar pruebas de autenticación exitosa/fallida de empleados.
   En iteración 004 MUST contemplar scaffolding frontend, integración con backend,
	flujos CRUD en UI y pruebas E2E críticas.
4. Ningún cambio pasa a implementación final si falla compilación, pruebas críticas o
	verificación manual mínima de endpoints documentados.

## Continuidad sobre Proyecto Existente

- Esta constitución aplica sobre el proyecto ya existente `DSW02-Practica01` y MUST
  guiar cambios incrementales, sin reconstrucción desde cero.
- El flujo de trabajo posterior MUST continuar con comandos Speckit en este orden:
  `/speckit.specify`, `/speckit.clarify` (opcional), `/speckit.plan`, `/speckit.tasks`,
  `/speckit.analyze` y `/speckit.implement`.
- Toda nueva iteración MUST partir de artefactos actuales y registrar deltas, no reinicios.
- La iteración 002 MUST concentrarse en autenticación de empleados y MUST NOT incluir
	inicio de CRUD de departamentos.
- La iteración 003 MUST iniciarse con actualización constitucional y luego continuar con
	`/speckit.specify` sobre el alcance incremental de Departamentos.
- La iteración 004 MUST iniciar con esta enmienda constitucional y luego continuar con
	`/speckit.specify` sobre el alcance incremental del frontend, sin reabrir decisiones
	resueltas de iteraciones 001-003 salvo conflicto explícito.

## Governance

Esta constitución prevalece sobre prácticas ad hoc del proyecto. Toda propuesta de
enmienda MUST incluir impacto en plantillas, motivación y plan de adopción.

Política de versionado de la constitución (SemVer):
- MAJOR: eliminación o redefinición incompatible de principios/gobernanza.
- MINOR: incorporación de nuevos principios o expansión normativa material.
- PATCH: aclaraciones editoriales sin cambio normativo.

Proceso de enmienda y cumplimiento:
- Toda PR MUST incluir revisión de cumplimiento constitucional.
- Las plantillas bajo `.specify/templates/` MUST mantenerse sincronizadas con cada enmienda.
- Si un directorio obligatorio de plantillas no existe, MUST registrarse en el Sync Impact Report.
- La revisión de cumplimiento MUST ejecutarse en planificación y antes de cerrar tareas.

**Version**: 2.3.0 | **Ratified**: 2026-02-26 | **Last Amended**: 2026-03-19
