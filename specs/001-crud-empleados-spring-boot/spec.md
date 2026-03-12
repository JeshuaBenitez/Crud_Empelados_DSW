# Feature Specification: CRUD de Empleados

**Feature Branch**: `001-crud-empleados-spring-boot`  
**Created**: 2026-02-26  
**Status**: Draft  
**Input**: User description: "Genera la constitución de un proyecto enfocado a backend que se centra en spring boot 3 con java 17. Utilice autenticación básica y conecte a postges con manejo de docker. la documentación debe estar en swagger. Crea un crud de empleados con los campos clave, nombre, dirección y teléfono. Donde clave sea PK y nombre, dirección y teléfono sea de 100 caracteres."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Alta y consulta de empleados (Priority: P1)

Como usuario autenticado, quiero crear empleados y consultarlos para registrar y recuperar su información básica.

**Why this priority**: Es el valor principal del sistema; sin alta y consulta no existe el CRUD mínimo funcional.

**Independent Test**: Se prueba de forma independiente autenticando con Basic Auth (`admin` / `admin123`), creando un empleado por API versionada (sin enviar clave), consultándolo por su clave generada con formato `EMP-<numero>` y listándolo con paginación.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado, **When** envía `POST /api/v1/empleados` con `nombre`, `direccion` y `telefono` válidos, **Then** el sistema genera la clave con formato `EMP-<numero>` y responde `201` con el recurso persistido.
2. **Given** un empleado existente, **When** un usuario autenticado llama `GET /api/v1/empleados/{clave}`, **Then** el sistema responde `200` con los datos del empleado.
3. **Given** empleados registrados, **When** un usuario autenticado llama `GET /api/v1/empleados?page=0&size=10`, **Then** el sistema responde `200` con resultados paginados.

---

### User Story 2 - Actualización y eliminación de empleados (Priority: P2)

Como usuario autenticado, quiero modificar y eliminar empleados para mantener la información actualizada.

**Why this priority**: Complementa la operación CRUD y permite mantenimiento operativo de datos.

**Independent Test**: Se prueba de forma independiente actualizando un empleado existente y luego eliminándolo por su clave.

**Acceptance Scenarios**:

1. **Given** un empleado existente, **When** un usuario autenticado llama `PUT /api/v1/empleados/{clave}` con `nombre`, `direccion` y `telefono` válidos, **Then** el sistema responde `200` con el empleado actualizado.
2. **Given** un empleado existente, **When** un usuario autenticado llama `DELETE /api/v1/empleados/{clave}`, **Then** el sistema responde `204` y el registro deja de existir.

---

### User Story 3 - Seguridad, persistencia y documentación (Priority: P3)

Como desarrollador/consumidor de API, quiero seguridad básica, base de datos PostgreSQL en Docker y documentación Swagger para desplegar y usar la API correctamente.

**Why this priority**: Son requisitos transversales de operación y adopción, necesarios para entorno local y consumo técnico.

**Independent Test**: Se prueba arrancando PostgreSQL con Docker, iniciando la API y validando autenticación y acceso a Swagger.

**Acceptance Scenarios**:

1. **Given** una solicitud sin credenciales, **When** accede a `GET /api/v1/empleados`, **Then** el sistema responde `401 Unauthorized`.
2. **Given** PostgreSQL en ejecución con Docker, **When** la aplicación inicia, **Then** conecta correctamente y aplica migraciones de esquema.
3. **Given** la aplicación en ejecución, **When** se abre Swagger UI, **Then** se visualiza la documentación OpenAPI de los endpoints CRUD versionados.

---

### Edge Cases

- ¿Qué ocurre cuando falla la generación del consecutivo autonumérico de la clave? El sistema debe responder con error de servidor y no persistir registros inconsistentes.
- ¿Qué ocurre cuando se intenta forzar una clave manual distinta del formato `EMP-<numero>`? El sistema debe rechazar la operación con error de validación.
- ¿Qué ocurre cuando se intenta crear un empleado con una combinación de PK compuesta (`prefijo`, `numero`) ya existente? El sistema debe rechazar la operación con `409 Conflict`.
- ¿Qué ocurre cuando los parámetros `page` o `size` son inválidos (negativos, no numéricos o `size > 100`)? Debe devolver error de validación.
- ¿Cómo maneja el sistema valores vacíos o nulos en `nombre`, `direccion` o `telefono`? Debe devolver error de validación.
- ¿Qué ocurre cuando `nombre`, `direccion` o `telefono` exceden 100 caracteres? Debe devolver error de validación.
- ¿Cómo responde el sistema si se intenta consultar, actualizar o eliminar una `clave` inexistente con formato `EMP-<numero>`? Debe devolver `404 Not Found`.
- ¿Cómo responde el sistema cuando faltan credenciales Basic Auth o son inválidas respecto a `admin` / `admin123`? Debe devolver `401 Unauthorized`.
- ¿Qué ocurre si PostgreSQL (Docker) no está disponible al iniciar o durante ejecución? La API debe fallar con error explícito de conexión y no simular persistencia.
- ¿Cómo se comporta Swagger cuando la seguridad está habilitada? La UI debe estar disponible y permitir autenticación para probar endpoints protegidos.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST exponer endpoint para crear empleados con los campos `nombre`, `direccion`, `telefono`.
- **FR-002**: El sistema MUST exponer endpoint versionado `GET /api/v1/empleados` con soporte de paginación vía parámetros `page` y `size`.
- **FR-003**: El sistema MUST exponer endpoint versionado para consultar un empleado por `clave` con formato `EMP-<numero>`.
- **FR-004**: El sistema MUST exponer endpoint versionado para actualizar un empleado existente por `clave` con formato `EMP-<numero>`.
- **FR-005**: El sistema MUST exponer endpoint versionado para eliminar un empleado existente por `clave` con formato `EMP-<numero>`.
- **FR-006**: La entidad Empleado MUST definir una clave primaria compuesta por `prefijo` y `numero`.
- **FR-007**: El campo lógico `clave` MUST construirse como `EMP-` + `numero` autogenerado y ser devuelto en respuestas API.
- **FR-008**: Los campos `nombre`, `direccion` y `telefono` MUST tener longitud máxima de 100 caracteres en validación de API y en esquema de base de datos.
- **FR-009**: Los endpoints `/api/v1/empleados/**` MUST requerir autenticación HTTP Basic.
- **FR-010**: El acceso inicial para pruebas MUST usar credenciales fijas `admin` / `admin123`.
- **FR-011**: El backend runtime MUST usar Spring Boot 3 y Java 17.
- **FR-012**: La persistencia MUST usar PostgreSQL.
- **FR-013**: El entorno local MUST incluir configuración Docker para levantar PostgreSQL.
- **FR-014**: El sistema MUST aplicar migraciones de base de datos al iniciar para crear la tabla de empleados.
- **FR-015**: La API MUST publicar documentación OpenAPI accesible mediante Swagger UI.
- **FR-016**: El sistema MUST devolver respuestas de error claras para validaciones, recursos no encontrados y errores de autenticación.
- **FR-017**: Al intentar crear un empleado con combinación de PK compuesta (`prefijo`, `numero`) existente, el sistema MUST responder `409 Conflict`.
- **FR-018**: La documentación Swagger/OpenAPI MUST reflejar rutas versionadas y parámetros de paginación del endpoint de listado.
- **FR-019**: El endpoint `GET /api/v1/empleados` MUST usar paginación con `page=0` y `size=10` por defecto, y MUST rechazar `size > 100`.
- **FR-020**: Los cambios de esta feature MUST aplicarse sobre la estructura actual del proyecto sin reconstrucción desde cero.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa a un empleado con PK compuesta por `prefijo` (valor fijo `EMP`) y `numero` (autonumérico), además de `clave` lógica derivada (`EMP-<numero>`), `nombre`, `direccion` y `telefono`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones CRUD (`crear`, `listar`, `obtener`, `actualizar`, `eliminar`) responde con códigos HTTP esperados en pruebas manuales de API autenticada.
- **SC-002**: La API rechaza el 100% de solicitudes a `/api/v1/empleados/**` sin credenciales válidas con `401`.
- **SC-003**: El 100% de intentos con `nombre`, `direccion` o `telefono` > 100 caracteres es rechazado por validación.
- **SC-004**: Al iniciar con PostgreSQL en Docker disponible, la aplicación arranca sin errores de conexión y la tabla `empleados` queda creada por migración.
- **SC-005**: Swagger UI muestra todos los endpoints CRUD y permite ejecutar al menos una operación autenticada de forma exitosa.
- **SC-006**: El 100% de altas exitosas genera una `clave` con patrón `EMP-<numero>` sin que el cliente envíe la clave.
- **SC-007**: El 100% de intentos de alta con combinación de PK compuesta duplicada responde con `409 Conflict`.
- **SC-008**: El 100% de llamadas al listado con `page` y `size` válidos retorna resultados paginados en rutas versionadas.
- **SC-009**: El 100% de pruebas de autenticación inicial con `admin` / `admin123` permite acceso a endpoints protegidos y Swagger documenta el esquema Basic.
- **SC-010**: El endpoint de listado aplica por defecto `page=0` y `size=10`, y rechaza `size > 100` con error de validación.
