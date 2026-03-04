<!--
Sync Impact Report
- Version change: 0.0.0 -> 1.0.0
- Modified principles:
	- [PRINCIPLE_1_NAME] -> I. Runtime Base Inmutable
	- [PRINCIPLE_2_NAME] -> II. Seguridad de Acceso Mínimo
	- [PRINCIPLE_3_NAME] -> III. Persistencia PostgreSQL Containerizada
	- [PRINCIPLE_4_NAME] -> IV. Contrato API en Swagger
	- [PRINCIPLE_5_NAME] -> V. Calidad Verificable y Trazabilidad
- Added sections:
	- Restricciones Técnicas Obligatorias
	- Flujo de Entrega y Puertas de Calidad
- Removed sections:
	- Ninguna
- Templates requiring updates:
	- ✅ .specify/templates/plan-template.md
	- ✅ .specify/templates/spec-template.md
	- ✅ .specify/templates/tasks-template.md
	- ⚠ pending: .specify/templates/commands/*.md (directorio no existe en este repositorio)
	- ✅ .github/prompts/*.md (verificado, sin referencias desactualizadas)
- Deferred TODOs:
	- Ninguno
-->

# DSW02-Practica01 Constitution

## Core Principles

### I. Runtime Base Inmutable
Todo componente backend MUST ejecutarse sobre Spring Boot 3 y Java 17. Las nuevas
funcionalidades MUST mantenerse dentro del stack estándar de Spring (Spring Web,
Spring Security, Spring Data JPA cuando aplique) y MUST evitar mezclas de frameworks
que dupliquen responsabilidades de runtime.
Rationale: un runtime único reduce riesgo operativo, deuda técnica y fricción de mantenimiento.

### II. Seguridad de Acceso Mínimo
Todo endpoint no público MUST estar protegido con autenticación básica HTTP gestionada
por Spring Security, con credenciales externas al código fuente. Las credenciales MUST
provenir de variables de entorno o secretos del entorno de ejecución. Ningún secreto
MUST quedar hardcodeado en repositorio ni en documentación versionada.
Rationale: el control de acceso mínimo y la externalización de secretos son base de seguridad.

### III. Persistencia PostgreSQL Containerizada
Toda persistencia relacional MUST usar PostgreSQL. Para desarrollo e integración local,
la base MUST ejecutarse en Docker (Docker Compose u orquestación equivalente) y la
aplicación MUST poder levantar contra esa instancia sin cambios de código. La
configuración de conexión MUST ser parametrizable por entorno.
Rationale: la paridad de entorno reduce incidencias entre desarrollo, pruebas y despliegue.

### IV. Contrato API en Swagger
Toda API HTTP MUST estar documentada y publicada en Swagger/OpenAPI. Cada endpoint
debe declarar operación, parámetros, respuestas esperadas y códigos de error. Ningún
cambio de contrato se considera completo sin actualización de la documentación Swagger
y validación de consistencia con la implementación.
Rationale: la documentación viva evita ambigüedad entre backend, QA y consumidores.

### V. Calidad Verificable y Trazabilidad
Cada historia implementada MUST incluir pruebas automatizadas de unidad y/o integración
proporcionales al riesgo, y MUST registrar decisiones técnicas relevantes en los artefactos
de especificación/plan/tareas. Toda entrega MUST validar arranque de aplicación,
conectividad a PostgreSQL en Docker y disponibilidad del endpoint de Swagger.
Rationale: sin evidencia verificable no existe criterio objetivo de completitud.

## Restricciones Técnicas Obligatorias

- El proyecto backend MUST estructurarse como servicio web Spring Boot.
- Java MUST fijarse en versión 17 en build y runtime.
- La autenticación básica MUST aplicarse al menos a rutas de negocio y administración.
- PostgreSQL MUST ser el motor de datos por defecto para cualquier módulo persistente.
- Docker MUST proveer entorno reproducible para base de datos en desarrollo.
- Swagger/OpenAPI MUST estar habilitado en ambientes no productivos como mínimo.

## Flujo de Entrega y Puertas de Calidad

1. Toda especificación MUST declarar requisitos de seguridad (Basic Auth), persistencia
	(PostgreSQL) y contrato API (Swagger).
2. Todo plan MUST incluir un Constitution Check explícito para stack, seguridad,
	base de datos containerizada y documentación API.
3. Toda lista de tareas MUST contemplar configuración Docker/PostgreSQL, seguridad,
	documentación Swagger y pruebas.
4. Ningún cambio pasa a implementación final si falla compilación, pruebas críticas o
	verificación manual mínima de endpoints documentados.

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

**Version**: 1.0.0 | **Ratified**: 2026-02-26 | **Last Amended**: 2026-02-26
