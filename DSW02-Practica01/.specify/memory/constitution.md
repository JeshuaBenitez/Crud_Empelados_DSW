<!--
Sync Impact Report
- Version change: 1.0.0 -> 2.0.0
- Modified principles:
	- [PRINCIPLE_1_NAME] -> I. Runtime Base Inmutable
	- [PRINCIPLE_2_NAME] -> II. Seguridad Básica Académica
	- [PRINCIPLE_3_NAME] -> III. Persistencia PostgreSQL Containerizada
	- [PRINCIPLE_4_NAME] -> IV. Contrato API Versionado en Swagger
	- [PRINCIPLE_5_NAME] -> V. Calidad Verificable, Paginación y Trazabilidad
- Added sections:
	- Restricciones Técnicas Obligatorias
	- Flujo de Entrega y Puertas de Calidad
	- Continuidad sobre Proyecto Existente
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

### II. Seguridad Básica Académica
Todo endpoint no público MUST estar protegido con autenticación básica HTTP gestionada
por Spring Security. Para este proyecto académico, la autenticación MUST usar credenciales
fijas con usuario `admin` y contraseña `admin123`, manteniendo comportamiento consistente
en entorno local y de evaluación.
Rationale: estandarizar credenciales reduce fricción en evaluación docente y pruebas funcionales.

### III. Persistencia PostgreSQL Containerizada
Toda persistencia relacional MUST usar PostgreSQL. Para desarrollo e integración local,
la base MUST ejecutarse en Docker (Docker Compose u orquestación equivalente) y la
aplicación MUST poder levantar contra esa instancia sin cambios de código. La
configuración de conexión MUST ser parametrizable por entorno.
Rationale: la paridad de entorno reduce incidencias entre desarrollo, pruebas y despliegue.

### IV. Contrato API Versionado en Swagger
Toda API HTTP MUST estar documentada y publicada en Swagger/OpenAPI. Cada endpoint
debe declarar operación, parámetros, respuestas esperadas y códigos de error. El contrato
MUST incluir versionado explícito de rutas (por ejemplo `/api/v1/...`). Ningún cambio
de contrato se considera completo sin actualización de la documentación Swagger y
validación de consistencia con la implementación.
Rationale: versionar explícitamente evita ruptura de clientes y mejora gobernanza del API.

### V. Calidad Verificable, Paginación y Trazabilidad
Cada historia implementada MUST incluir pruebas automatizadas de unidad y/o integración
proporcionales al riesgo, y MUST registrar decisiones técnicas relevantes en los artefactos
de especificación/plan/tareas. Toda entrega MUST validar arranque de aplicación,
conectividad a PostgreSQL en Docker y disponibilidad del endpoint de Swagger. Todo
endpoint de listado GET MUST soportar paginación explícita y documentada.
Rationale: sin evidencia verificable y sin control de volumen en listados no existe criterio objetivo de completitud.

## Restricciones Técnicas Obligatorias

- El proyecto backend MUST estructurarse como servicio web Spring Boot.
- Java MUST fijarse en versión 17 en build y runtime.
- La autenticación básica MUST aplicarse al menos a rutas de negocio y administración.
- El acceso básico MUST operar con usuario `admin` y contraseña `admin123`.
- PostgreSQL MUST ser el motor de datos por defecto para cualquier módulo persistente.
- Docker MUST proveer entorno reproducible para base de datos en desarrollo.
- Swagger/OpenAPI MUST estar habilitado en ambientes no productivos como mínimo.
- Los endpoints HTTP MUST incluir versión explícita en la ruta (`/v1` o equivalente).
- Los endpoints GET de listado MUST exponer parámetros de paginación.

## Flujo de Entrega y Puertas de Calidad

1. Toda especificación MUST declarar requisitos de seguridad (Basic Auth), persistencia
	(PostgreSQL) y contrato API (Swagger + versionado explícito).
2. Todo plan MUST incluir un Constitution Check explícito para stack, seguridad,
	base de datos containerizada y documentación API.
3. Toda lista de tareas MUST contemplar configuración Docker/PostgreSQL, seguridad,
	documentación Swagger, versionado de endpoints, paginación y pruebas.
4. Ningún cambio pasa a implementación final si falla compilación, pruebas críticas o
	verificación manual mínima de endpoints documentados.

## Continuidad sobre Proyecto Existente

- Esta constitución aplica sobre el proyecto ya existente `DSW02-Practica01` y MUST
  guiar cambios incrementales, sin reconstrucción desde cero.
- El flujo de trabajo posterior MUST continuar con comandos Speckit en este orden:
  `/speckit.specify`, `/speckit.clarify` (opcional), `/speckit.plan`, `/speckit.tasks`,
  `/speckit.analyze` y `/speckit.implement`.
- Toda nueva iteración MUST partir de artefactos actuales y registrar deltas, no reinicios.

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

**Version**: 2.0.0 | **Ratified**: 2026-02-26 | **Last Amended**: 2026-03-09
