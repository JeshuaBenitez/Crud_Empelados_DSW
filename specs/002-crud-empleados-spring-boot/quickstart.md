# Quickstart: Autenticacion Empleados (Iteracion 002)

## Objetivo

Validar de forma operativa la autenticacion por `correo` + `password` de empleados con JWT, manteniendo compatibilidad con PostgreSQL en Docker y sin incluir funcionalidades de departamentos.

## Matriz de Riesgos y Mitigaciones

| ID | Riesgo | Impacto | Probabilidad | Mitigacion | Dueno sugerido | Evidencia de cierre |
|---|---|---|---|---|---|---|
| R-001 | Bypass entre Basic y JWT en rutas de negocio | Alto | Media | En `SecurityConfig`, forzar `JWT only` en `/api/v1/empleados/**` y `Basic only` en rutas admin. Agregar tests negativos de bypass. | Backend Security | Tests de seguridad US2 en verde (`401/403` esperados) |
| R-002 | Falla de migracion por datos historicos inconsistentes | Alto | Alta | Ejecutar script obligatorio de carga/saneamiento previo (`T005A`) antes de constraints finales (`T005`). | Backend + DBA | Log de migracion limpio y verificacion de constraints aplicadas |
| R-003 | Ausencia de datos para probar login en local | Medio | Alta | Cargar seed inicial obligatoria (`T005B`) con usuario de prueba y password hasheado. | Backend | Login exitoso con seed en entorno local |
| R-004 | Password almacenado de forma insegura | Alto | Media | Hash obligatorio con BCrypt, sin persistir texto plano, sin loggear credenciales. | Backend Security | Pruebas unitarias de hash/verify + revision de DB |
| R-005 | Tokens JWT inseguros o mal configurados | Alto | Media | Secret por entorno, expiracion definida, validacion estricta de firma/exp y claims minimos. | Backend Security | Tests token valido/invalido/expirado en verde |
| R-006 | Autorizacion incompleta de recurso propio | Alto | Media | Validar ownership en servicio: empleado autenticado solo accede a su recurso; terceros retornan `403`. | Backend API | Test `OwnResourceAuthorizationIntegrationTest` en verde |
| R-007 | Filtracion de informacion en errores de login | Medio | Media | Mensaje uniforme para credenciales invalidas, auditoria interna separada del mensaje cliente. | Backend API | Validacion de respuestas de login fallido (`401`) |
| R-008 | Swagger deja de ser usable para pruebas | Medio | Media | Mantener Swagger publico en local y documentar esquema Bearer + endpoint login en OpenAPI. | Backend API | Prueba manual desde Swagger: login + endpoint protegido |
| R-009 | Regresion del CRUD existente por cambios de seguridad | Alto | Media | Ejecutar regresion de endpoints de empleados tras integrar JWT y reglas de acceso. | QA/Backend | Suite de pruebas + smoke test completados |
| R-010 | Desbordamiento de alcance hacia departamentos en iteracion 002 | Medio | Baja | Aplicar gate de alcance y validacion explicita de no inclusion (`T036`). | Tech Lead | Registro de cumplimiento SC-010 y revisiones sin artefactos de departamentos |

## Criterios Minimos de Salida

- Login de empleado funciona con datos persistidos y password hasheado.
- `/api/v1/empleados/**` acepta JWT y rechaza Basic en negocio.
- Rutas administrativas mantienen Basic segun configuracion.
- Seed y script de carga previa son ejecutables en entorno local.
- Swagger documenta login + Bearer y permite prueba funcional.
- No se agregan endpoints/modelos de departamentos en esta iteracion.

## Alcance CRUD de Empleados en Iteracion 002

- Esta iteracion **no** se considera CRUD completo de empleados.
- `POST /api/v1/empleados` permanece bloqueado y debe responder `403 Forbidden`.
- El foco funcional de la iteracion es login JWT y acceso de empleado a su propio recurso.
