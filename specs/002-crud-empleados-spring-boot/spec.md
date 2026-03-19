# Feature Specification: Autenticacion de Empleados por Correo y Password

**Feature Branch**: `002-crud-empleados-spring-boot`  
**Created**: 2026-03-12  
**Status**: Draft  
**Input**: User description: "Incorporar autenticacion por correo y password de empleados persistidos en PostgreSQL, evaluar JWT, mantener integracion con backend existente, Docker y Swagger/OpenAPI, sin incluir departamentos."

## Clarifications

### Session 2026-03-12

- Autenticacion para iteracion 002: `JWT` confirmado como mecanismo principal para empleados.
- Correo de empleado: debe ser unico en base de datos.
- Endpoint de login confirmado: `POST /api/v1/auth/empleados/login`.
- Swagger/OpenAPI: se mantiene acceso publico para pruebas.
- Endpoints de empleados: `JWT only` sobre `/api/v1/empleados/**`.
- Autenticacion Basic: solo para rutas administrativas (no para rutas de negocio de empleados).
- Password de empleados: hash seguro obligatorio (alineado con constitucion v2.1.0).
- Migracion de datos: script de carga obligatorio previo a endurecer restricciones de autenticacion.
- Seed inicial: obligatoria para pruebas locales de login y autorizacion.
- Alcance de autorizacion en iteracion 002: empleado autenticado accede solo a su propio recurso.
- Alcance CRUD en iteracion 002: `POST /api/v1/empleados` permanece bloqueado con `403` (alta de empleados fuera de alcance de esta iteracion).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Login de empleado con credenciales persistidas (Priority: P1)

Como empleado, quiero autenticarme con mi correo y contraseña para obtener acceso seguro al backend.

**Why this priority**: Es el objetivo funcional principal de la iteracion 002 y habilita el resto de controles de acceso.

**Independent Test**: Se prueba creando o disponiendo un empleado con correo/password hash en base de datos, invocando el endpoint de login y verificando respuesta de autenticacion exitosa y fallida.

**Acceptance Scenarios**:

1. **Given** un empleado activo con correo registrado y password valida en PostgreSQL, **When** llama `POST /api/v1/auth/empleados/login`, **Then** el sistema responde `200` con credencial de sesion (JWT) y metadatos de autenticacion.
2. **Given** credenciales invalidas (correo inexistente o password incorrecta), **When** llama `POST /api/v1/auth/empleados/login`, **Then** el sistema responde `401 Unauthorized` sin exponer datos sensibles.
3. **Given** un request de login mal formado, **When** falta `correo` o `password`, **Then** el sistema responde `400 Bad Request` con mensaje de validacion.

---

### User Story 2 - Consumo de endpoints protegidos con identidad de empleado (Priority: P2)

Como API consumer, quiero usar la credencial emitida en login para acceder a endpoints protegidos de empleados sin depender del Basic Auth global en rutas de negocio.

**Why this priority**: Garantiza que la autenticacion de negocio (empleado) quede integrada y no se limite a credenciales administrativas globales.

**Independent Test**: Se prueba acceso a endpoint protegido con token valido y rechazo con token invalido/ausente.

**Acceptance Scenarios**:

1. **Given** un token valido emitido por login, **When** el empleado invoca su propio recurso protegido con `Authorization: Bearer <token>`, **Then** el sistema responde exitosamente (`2xx`).
2. **Given** token ausente, invalido o expirado, **When** se invoca endpoint protegido de empleados, **Then** el sistema responde `401 Unauthorized`.
3. **Given** un empleado autenticado, **When** intenta acceder al recurso de otro empleado, **Then** el sistema responde `403 Forbidden`.
4. **Given** endpoints administrativos/documentales definidos por el proyecto, **When** se invocan, **Then** se mantiene compatibilidad con Basic para admin y Swagger usable.

---

### User Story 3 - Documentacion y operacion local reproducible (Priority: P3)

Como desarrollador, quiero que Swagger/OpenAPI y Docker/PostgreSQL sigan funcionando con la nueva autenticacion para validar la feature localmente.

**Why this priority**: Evita regresiones operativas y preserva el flujo de pruebas del curso.

**Independent Test**: Se prueba levantando stack local con Docker/PostgreSQL, ejecutando backend y validando login desde Swagger UI o curl.

**Acceptance Scenarios**:

1. **Given** el entorno local con PostgreSQL en Docker, **When** la aplicacion inicia, **Then** la autenticacion por empleados consulta datos persistidos sin romper arranque.
2. **Given** Swagger UI habilitado, **When** se documentan endpoints de autenticacion, **Then** es posible probar login y consumo de endpoints protegidos desde la UI.
3. **Given** la feature 002 activa, **When** se revisa alcance funcional, **Then** no se incluyen endpoints ni modelo de departamentos.

---

### Edge Cases

- ¿Que ocurre si dos empleados comparten el mismo correo en base de datos? El sistema debe impedirlo con restriccion de unicidad y manejar conflicto de datos.
- ¿Que ocurre si el password almacenado no cumple formato hash esperado? El sistema debe rechazar autenticacion y registrar evento de error tecnico.
- ¿Como responde el sistema ante intentos repetidos fallidos de login? Debe responder consistentemente sin filtrar si el correo existe o no.
- ¿Que ocurre cuando el token JWT expira? Debe devolver `401 Unauthorized` y requerir nuevo login.
- ¿Que ocurre si PostgreSQL no esta disponible al autenticar? Debe fallar con error explicito controlado sin generar tokens inconsistentes.
- ¿Que ocurre si falta ejecutar el script obligatorio de carga previa? La migracion debe fallar de forma controlada y documentada.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST exponer autenticacion de empleados basada en `correo` y `password`.
- **FR-002**: El sistema MUST validar credenciales de empleados contra datos persistidos en PostgreSQL.
- **FR-003**: El sistema MUST almacenar password de empleados con hash seguro (no texto plano).
- **FR-004**: El sistema MUST exponer endpoint `POST /api/v1/auth/empleados/login` para autenticacion.
- **FR-005**: El endpoint de login MUST aceptar payload con `correo` y `password`.
- **FR-006**: En autenticacion exitosa, el sistema MUST devolver credencial de sesion reutilizable para endpoints protegidos.
- **FR-007**: Para esta iteracion, la credencial de sesion seleccionada MUST ser JWT por adecuacion al API stateless.
- **FR-008**: El sistema MUST rechazar credenciales invalidas con `401 Unauthorized` y mensajes no reveladores.
- **FR-009**: El sistema MUST rechazar payload de login invalido con `400 Bad Request`.
- **FR-010**: El sistema MUST permitir acceso a endpoints protegidos de empleados usando `Authorization: Bearer <token>`.
- **FR-011**: Las rutas de negocio de empleados (`/api/v1/empleados/**`) MUST aceptar autenticacion JWT y MUST NOT autenticarse por Basic.
- **FR-012**: Las rutas administrativas MUST mantener autenticacion Basic (`admin/admin123`).
- **FR-013**: Swagger/OpenAPI MUST documentar endpoint de login, esquema Bearer JWT y errores de autenticacion.
- **FR-014**: Antes de activar restricciones finales de autenticacion, el proceso MUST ejecutar script de carga de datos obligatorio.
- **FR-015**: La feature MUST incluir seed inicial de empleado para pruebas locales de login/autorizacion.
- **FR-016**: El empleado autenticado MUST acceder solo a su propio recurso y MUST recibir `403` en recursos de terceros.
- **FR-017**: La feature MUST integrarse sobre el backend actual sin regenerar proyecto ni reestructurar arquitectura base.
- **FR-018**: La solucion MUST mantener compatibilidad con ejecucion local en Docker + PostgreSQL.
- **FR-019**: Esta iteracion MUST NOT incluir CRUD ni modelo de departamentos.
- **FR-020**: En iteracion 002, `POST /api/v1/empleados` MUST permanecer deshabilitado y responder `403 Forbidden` para evitar altas fuera de alcance.

### Key Entities *(include if feature involves data)*

- **EmpleadoAuth**: Vista de autenticacion del empleado que incluye `id`, `correo`, `passwordHash`, estado de cuenta (activo/inactivo) y metadatos minimos para autorizacion.
- **AuthSessionToken**: Credencial emitida en login con claims de identidad de empleado, ventana de validez y tipo de token (`Bearer`).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de logins con credenciales validas de empleados registrados devuelve `200` y token utilizable.
- **SC-002**: El 100% de logins con credenciales invalidas devuelve `401` sin revelar si el correo existe.
- **SC-003**: El 100% de requests de login con payload incompleto/invalido devuelve `400`.
- **SC-004**: El 100% de accesos a endpoint protegido con token valido devuelve respuesta `2xx` esperada.
- **SC-005**: El 100% de accesos con token invalido/ausente devuelve `401`.
- **SC-006**: Swagger/OpenAPI permite ejecutar al menos una prueba de login y una invocacion autenticada con Bearer JWT.
- **SC-007**: La aplicacion inicia correctamente contra PostgreSQL en Docker sin regresion de conectividad.
- **SC-008**: El 100% de accesos de empleado a recursos de terceros devuelve `403`.
- **SC-009**: La carga obligatoria previa y seed inicial quedan ejecutables y verificadas en entorno local.
- **SC-010**: No se implementan artefactos funcionales de departamentos en la iteracion 002.
- **SC-011**: El 100% de intentos de `POST /api/v1/empleados` en iteracion 002 devuelve `403 Forbidden`.
