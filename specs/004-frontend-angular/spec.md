# Feature Specification: Frontend Angular para Login y CRUD de Empleados/Departamentos

**Feature Branch**: `004-frontend-angular`  
**Created**: 2026-03-19  
**Status**: Draft  
**Input**: User description: "Construir el frontend del sistema usando Angular 22 LTS, Tailwind CSS y Cypress, consumiendo el backend ya existente con JWT, CRUD de empleados y CRUD de departamentos, sin regenerar backend."

## Clarifications

### Session 2026-03-19

- Esta iteracion se implementa de forma incremental sobre el proyecto existente.
- El frontend MUST desarrollarse en `frontend-angular/`.
- La especificacion de iteracion 004 MUST almacenarse en `specs/004-frontend-angular/`.
- El backend existente se considera fuente de verdad para reglas de negocio y contratos API.
- La autenticacion frontend MUST usar `POST /api/v1/auth/empleados/login` y JWT emitido por backend.
- El frontend MUST incluir modulos/pantallas para login, empleados y departamentos.
- El frontend MUST usar Tailwind CSS para una interfaz limpia y moderna.
- La calidad frontend MUST contemplar compatibilidad con Cypress para E2E.
- La integracion MUST mantener compatibilidad con JWT, Docker y arquitectura actual.
- Esta iteracion MUST NOT regenerar backend ni modificarlo innecesariamente.

### Session 2026-03-19 (Clarify)

- **DEC-001 (JWT storage)**: el token JWT se almacenara en `sessionStorage` (no `localStorage`) para reducir persistencia entre sesiones del navegador; no se usaran cookies HttpOnly en esta iteracion por no existir flujo backend cookie-based.
- **DEC-002 (main navigation)**: la navegacion principal sera `login -> dashboard -> empleados | departamentos`.
- **DEC-003 (authenticated layout)**: tras login exitoso existira layout comun autenticado con `navbar` superior y `sidebar` lateral colapsable para navegacion de modulos.
- **DEC-004 (UI pattern sharing)**: empleados y departamentos compartiran patron comun `tabla + acciones + formulario reutilizable` para coherencia y menor deuda tecnica.
- **DEC-005 (form interaction model)**: crear/editar se implementara con formularios en vista separada por ruta (no modal) para simplificar validaciones, navegacion y pruebas E2E.
- **DEC-006 (load/error/empty states)**: cada vista de listado y formulario tendra estados explicitos de `loading`, `error` y `empty`; se definira componente reutilizable para mensajes y reintento.
- **DEC-007 (route protection)**: se requiere proteccion de rutas con `AuthGuard`; rutas privadas redirigen a login cuando no existe sesion valida.
- **DEC-008 (explicit logout)**: se implementa logout explicito en layout autenticado; al ejecutarse limpia sesion y redirige a login.
- **DEC-009 (service architecture)**: se definiran servicios separados por dominio: `AuthService`, `EmpleadosService`, `DepartamentosService`, mas capa compartida de cliente HTTP/interceptor.
- **DEC-010 (critical extra ambiguities resolved)**:
	- URL base API se define por archivos de entorno (`environment`) y no hardcodeada.
	- Se usa interceptor HTTP para inyectar Bearer token y capturar `401` global.
	- Paginacion UI se alinea a defaults backend (`page=0`, `size=10`) y limite maximo configurable en frontend.
	- Mensajeria de errores distingue `400/401/403/404/409/5xx` con textos orientados a usuario.
	- Alcance de autorizacion frontend asume rol unico de empleado segun backend actual (sin RBAC adicional en 004).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Login de empleado y gestion de sesion JWT (Priority: P1)

Como empleado, quiero iniciar sesion desde el frontend para acceder de forma segura a los modulos de empleados y departamentos.

**Why this priority**: Sin autenticacion no se puede habilitar acceso controlado a funcionalidades de negocio protegidas.

**Independent Test**: Se prueba login exitoso y fallido desde UI, persistencia de token para la sesion, y redireccion a vista principal al autenticar.

**Acceptance Scenarios**:

1. **Given** un empleado con credenciales validas en backend, **When** envia login desde la pantalla, **Then** el frontend obtiene JWT, guarda estado de sesion y navega al modulo principal.
2. **Given** credenciales invalidas, **When** intenta login, **Then** el frontend muestra mensaje de error claro sin exponer informacion sensible.
3. **Given** una sesion autenticada, **When** consume endpoints protegidos, **Then** el frontend envia `Authorization: Bearer <token>` en requests correspondientes.
4. **Given** token expirado o invalido, **When** backend responde `401`, **Then** el frontend limpia sesion y redirige a login con retroalimentacion adecuada.

---

### User Story 2 - Gestion de empleados desde interfaz web (Priority: P1)

Como usuario autenticado, quiero listar, crear, editar y eliminar empleados desde una UI para operar el dominio de empleados sin usar cliente API manual.

**Why this priority**: El modulo de empleados es capacidad central del sistema y debe tener flujo end-to-end en frontend.

**Independent Test**: Se prueba listado paginado y operaciones CRUD desde UI, incluyendo manejo de errores backend y estados de carga/vacio.

**Acceptance Scenarios**:

1. **Given** sesion autenticada, **When** entra al modulo empleados, **Then** visualiza listado paginado segun respuesta backend.
2. **Given** datos validos para alta/edicion segun reglas vigentes, **When** envia formulario, **Then** frontend refleja exito y actualiza listado.
3. **Given** respuesta de error de negocio o validacion desde backend, **When** intenta alta/edicion/eliminacion invalida, **Then** frontend muestra mensaje comprensible y mantiene estado consistente.
4. **Given** restricciones existentes del backend (por ejemplo operaciones fuera de alcance), **When** una accion retorna `403`/`409`, **Then** la UI comunica la limitacion sin romper navegacion.

---

### User Story 3 - Gestion de departamentos desde interfaz web (Priority: P1)

Como usuario autenticado, quiero listar, crear, editar y eliminar departamentos para administrar estructura organizacional integrada con empleados.

**Why this priority**: La iteracion 003 habilito CRUD de departamentos y esta iteracion debe completar su consumo desde frontend.

**Independent Test**: Se prueba CRUD completo de departamentos desde UI con JWT y manejo de conflictos de negocio.

**Acceptance Scenarios**:

1. **Given** sesion autenticada, **When** navega al modulo departamentos, **Then** visualiza listado paginado obtenido de backend.
2. **Given** datos validos, **When** crea o actualiza un departamento, **Then** la UI confirma operacion y actualiza datos visibles.
3. **Given** departamento con empleados asociados, **When** intenta eliminarlo y backend retorna `409`, **Then** frontend muestra conflicto de negocio y conserva consistencia visual.
4. **Given** token invalido o ausente durante uso del modulo, **When** backend responde `401`, **Then** frontend ejecuta flujo de sesion no valida.

---

### User Story 4 - Experiencia de usuario moderna y navegacion clara (Priority: P2)

Como usuario final, quiero una interfaz limpia y coherente para completar tareas con claridad.

**Why this priority**: Mejora usabilidad, reduce errores operativos y facilita adopcion del sistema.

**Independent Test**: Se valida navegacion entre login/empleados/departamentos, estados de carga/vacio/error y consistencia visual con Tailwind.

**Acceptance Scenarios**:

1. **Given** usuario autenticado, **When** usa menu o rutas principales, **Then** navega claramente entre login, empleados y departamentos.
2. **Given** llamadas en progreso, **When** espera respuesta backend, **Then** la UI muestra estados de carga visibles.
3. **Given** respuesta sin datos, **When** abre listado vacio, **Then** la UI muestra estado vacio informativo.
4. **Given** errores de red o servidor, **When** ocurren fallos, **Then** la UI presenta mensajes claros y accion sugerida para reintento.

---

### User Story 5 - Base de calidad automatizable con Cypress (Priority: P2)

Como equipo de desarrollo, quiero que el frontend sea compatible con Cypress para automatizar regresiones criticas.

**Why this priority**: Asegura confiabilidad de flujos clave y reduce regresiones en integracion frontend-backend.

**Independent Test**: Se ejecutan pruebas E2E Cypress para login, navegacion y al menos un flujo CRUD representativo de cada modulo.

**Acceptance Scenarios**:

1. **Given** entorno local operativo, **When** se ejecuta suite Cypress, **Then** corre al menos flujo de login exitoso/fallido.
2. **Given** sesion autenticada, **When** se ejecuta flujo CRUD representativo de empleados, **Then** la prueba valida comportamiento esperado de UI + API.
3. **Given** sesion autenticada, **When** se ejecuta flujo CRUD representativo de departamentos, **Then** la prueba valida manejo de exito y conflicto.
4. **Given** cambios incrementales en frontend, **When** se corre Cypress en CI/local, **Then** la deteccion de regresiones es reproducible.

---

### Edge Cases

- Login con campos vacios o formato invalido en correo debe impedir envio o mostrar validacion clara.
- Backend no disponible o timeout durante login/listados debe mostrar error recuperable sin colgar UI.
- Respuestas `401` consecutivas por token expirado en multiples requests deben resolverse con una sola transicion controlada a login.
- Respuestas `403` por reglas del backend deben mostrarse como restriccion funcional, no como fallo tecnico generico.
- Respuestas `409` de negocio (ej. duplicidad/conflicto de eliminacion) deben mapearse a mensajes entendibles para usuario.
- Listados con 0 resultados deben conservar controles de filtro/paginacion sin romper layout.
- Navegacion directa a ruta protegida sin sesion debe redirigir a login.
- Datos de API incompletos o campos opcionales ausentes deben manejarse de forma defensiva en renderizado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El frontend MUST implementarse en Angular 22 LTS dentro de `frontend-angular/`.
- **FR-002**: La iteracion 004 MUST mantener su especificacion en `specs/004-frontend-angular/`.
- **FR-003**: El frontend MUST consumir exclusivamente la API backend existente y versionada (`/api/v1/...`).
- **FR-004**: El frontend MUST implementar pantalla de login usando `POST /api/v1/auth/empleados/login`.
- **FR-005**: En login exitoso, el frontend MUST persistir estado de sesion JWT para uso en requests protegidos durante la sesion.
- **FR-006**: El frontend MUST adjuntar `Authorization: Bearer <token>` a llamadas protegidas de empleados y departamentos.
- **FR-007**: El frontend MUST manejar respuestas `401` limpiando sesion y redirigiendo a login de forma consistente.
- **FR-008**: El frontend MUST implementar modulo/pantallas para CRUD de empleados consumiendo endpoints existentes.
- **FR-009**: El frontend MUST implementar modulo/pantallas para CRUD de departamentos consumiendo endpoints existentes.
- **FR-010**: El frontend MUST respetar reglas de negocio ya implementadas en backend, incluyendo errores de validacion/autorizacion/conflicto.
- **FR-011**: El frontend MUST incluir navegacion clara entre login, empleados y departamentos.
- **FR-012**: El frontend MUST mostrar estados visuales de carga, error y vacio en vistas de listado/formulario.
- **FR-013**: La interfaz MUST usar Tailwind CSS como base de estilos para consistencia visual.
- **FR-014**: El frontend MUST externalizar configuracion de URL base API por entorno (local/containerizado).
- **FR-015**: La iteracion MUST contemplar compatibilidad con Cypress para pruebas E2E.
- **FR-016**: La suite Cypress MUST cubrir al menos login y un flujo CRUD representativo de empleados y departamentos.
- **FR-017**: La implementacion frontend MUST ser incremental y MUST NOT regenerar el backend existente.
- **FR-018**: La iteracion 004 MUST NOT modificar backend innecesariamente; cualquier ajuste backend debe justificarse por incompatibilidad contractual explicita.
- **FR-019**: El frontend MUST mantener compatibilidad operativa con arquitectura actual basada en JWT, Docker y backend Spring Boot.
- **FR-020**: El frontend MUST almacenar JWT en `sessionStorage` durante la sesion activa.
- **FR-021**: El frontend MUST definir rutas `login`, `dashboard`, `empleados` y `departamentos` con navegacion coherente.
- **FR-022**: El area autenticada MUST usar layout comun con `navbar` y `sidebar` para navegacion principal.
- **FR-023**: Empleados y departamentos MUST reutilizar un patron de UI comun de listado y formulario.
- **FR-024**: Formularios de alta/edicion MUST implementarse en vistas por ruta separada y no en modales en esta iteracion.
- **FR-025**: El frontend MUST implementar componentes/plantillas reutilizables para estados `loading`, `error` y `empty`.
- **FR-026**: Las rutas protegidas MUST usar `AuthGuard` y redireccionar a login cuando no haya sesion valida.
- **FR-027**: El frontend MUST exponer accion de logout explicito en el layout autenticado.
- **FR-028**: La arquitectura de consumo API MUST separar servicios de `auth`, `empleados` y `departamentos`.
- **FR-029**: El frontend MUST usar interceptor HTTP para adjuntar JWT y gestionar errores `401` de forma global.
- **FR-030**: La configuracion de URL base API MUST definirse por entorno y MUST NOT hardcodearse en componentes.

### Key Entities *(include if feature involves data)*

- **FrontendSession**: Estado de sesion en cliente que encapsula token JWT, expiracion/logica de validez y estado de autenticacion.
- **EmpleadoViewModel**: Representacion de empleado para UI (listado/formulario) alineada al contrato API existente.
- **DepartamentoViewModel**: Representacion de departamento para UI (listado/formulario) alineada al contrato API existente.
- **ApiErrorViewModel**: Estructura normalizada de error para mapear respuestas backend (`400/401/403/404/409/5xx`) a mensajes de interfaz.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de flujos de login validos en UI obtiene sesion autenticada y navegacion a modulo protegido.
- **SC-002**: El 100% de intentos de login invalidos muestra error claro sin bloquear la aplicacion.
- **SC-003**: El 100% de requests protegidos desde frontend incluyen JWT cuando hay sesion activa.
- **SC-004**: El 100% de modulos de empleados y departamentos permite completar flujo CRUD definido por alcance y refleja respuestas backend en UI.
- **SC-005**: El 100% de escenarios de error clave (`401`, `403`, `409`, `5xx`) presenta feedback visual comprensible y estado consistente.
- **SC-006**: La navegacion principal entre login, empleados y departamentos se completa sin rutas rotas en pruebas manuales base.
- **SC-007**: Cypress ejecuta exitosamente pruebas E2E minimas de login + CRUD representativo de empleados + CRUD representativo de departamentos.
- **SC-008**: La iteracion 004 se integra sin regenerar backend ni alterar innecesariamente su estructura.
- **SC-009**: La solucion frontend mantiene compatibilidad con entorno local/containerizado existente del proyecto.
