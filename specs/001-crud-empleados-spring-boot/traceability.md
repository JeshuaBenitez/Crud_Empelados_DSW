# Matriz de Trazabilidad

**Feature**: `001-crud-empleados-spring-boot`  
**Propósito**: asegurar cobertura 1:1 entre requisitos del `spec.md` y tareas ejecutables del `tasks.md` antes de `/speckit.implement`.

## Reglas de lectura

- `Cobertura` = tarea(s) que implementan o validan el requisito.
- `Estado` = evaluación documental de trazabilidad (no ejecución).
- IDs de tarea referencian `tasks.md` (T001..T035).

---

## Trazabilidad de Functional Requirements (FR)

| Requisito | Cobertura en tareas | Estado |
|---|---|---|
| FR-001 Crear empleados con `nombre`, `direccion`, `telefono` | T013, T015, T016 | ✅ Cubierto |
| FR-002 `GET /api/v1/empleados` con `page` y `size` | T014, T015, T016, T026 | ✅ Cubierto |
| FR-003 Obtener por `clave` `EMP-<numero>` | T013, T016 | ✅ Cubierto |
| FR-004 Actualizar por `clave` versionada | T019, T020, T021 | ✅ Cubierto |
| FR-005 Eliminar por `clave` versionada | T019, T020, T021 | ✅ Cubierto |
| FR-006 PK compuesta (`prefijo`,`numero`) | T005, T006, T007, T008 | ✅ Cubierto |
| FR-007 Clave lógica `EMP-<numero>` autogenerada | T012, T013, T015, T017 | ✅ Cubierto |
| FR-008 Longitud máxima 100 (`nombre`,`direccion`,`telefono`) | T007, T033 | ✅ Cubierto |
| FR-009 `/api/v1/empleados/**` protegido con Basic Auth | T009, T023 | ✅ Cubierto |
| FR-010 Credenciales fijas `admin/admin123` | T002, T009, T023, T028 | ✅ Cubierto |
| FR-011 Runtime Spring Boot 3 + Java 17 | T001 | ✅ Cubierto |
| FR-012 Persistencia PostgreSQL | T001, T003, T025, T027 | ✅ Cubierto |
| FR-013 Docker para PostgreSQL en local | T003, T027, T032 | ✅ Cubierto |
| FR-014 Migraciones al iniciar | T005, T025 | ✅ Cubierto |
| FR-015 OpenAPI en Swagger UI | T011, T024, T026 | ✅ Cubierto |
| FR-016 Errores claros (validación/404/401/etc.) | T010, T022, T023, T033, T034 | ✅ Cubierto |
| FR-017 Duplicado de PK compuesta responde `409` | T017, T035 | ✅ Cubierto |
| FR-018 Swagger refleja rutas versionadas y paginación | T011, T024, T026, T028 | ✅ Cubierto |
| FR-019 Defaults `page=0`, `size=10` y rechazo `size>100` | T014, T015, T034 | ✅ Cubierto |
| FR-020 Cambios incrementales sin reconstruir proyecto | T001, T004, T027 | ✅ Cubierto |

---

## Trazabilidad de Success Criteria (SC)

| Criterio | Cobertura en tareas | Estado |
|---|---|---|
| SC-001 CRUD completo con códigos esperados | T013, T014, T019, T020, T021, T029, T031 | ✅ Cubierto |
| SC-002 401 en `/api/v1/empleados/**` sin credenciales válidas | T023, T031 | ✅ Cubierto |
| SC-003 Rechazo de campos > 100 | T033, T031 | ✅ Cubierto |
| SC-004 Arranque con PostgreSQL Docker + migración aplicada | T025, T027, T032 | ✅ Cubierto |
| SC-005 Swagger muestra CRUD y permite operación autenticada | T024, T026, T028, T031 | ✅ Cubierto |
| SC-006 Altas generan `EMP-<numero>` sin clave enviada | T012, T013, T017, T031 | ✅ Cubierto |
| SC-007 Duplicado de PK compuesta responde `409` | T017, T035, T031 | ✅ Cubierto |
| SC-008 Listado paginado en rutas versionadas con params válidos | T014, T015, T016, T031 | ✅ Cubierto |
| SC-009 Acceso con `admin/admin123` + Basic documentado | T002, T009, T023, T024, T026, T031 | ✅ Cubierto |
| SC-010 Defaults de paginación y rechazo `size>100` | T014, T015, T034, T031 | ✅ Cubierto |

---

## Hallazgos de análisis

- No se detectan requisitos FR/SC sin cobertura en `tasks.md`.
- Las tareas T033, T034 y T035 cierran vacíos de validación negativa y conflicto (`409`) detectados en revisión previa.
- La trazabilidad está lista para continuar con `/speckit.implement`.
