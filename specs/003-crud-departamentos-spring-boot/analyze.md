# Analyze: Riesgos y Advertencias de Implementacion (Iteracion 003)

**Scope**: CRUD de departamentos + relacion con empleados, implementacion incremental sobre proyecto existente.

## Resumen Ejecutivo

La iteracion 003 es viable con el plan actual, pero presenta riesgos altos en tres frentes:

1. Migraciones de datos (backfill y no-null final).
2. Regresion de seguridad JWT en rutas existentes y nuevas.
3. Inconsistencia de contrato/respuesta por ciclos JSON y cambios de DTO.

Si se respetan los safety gates definidos en tareas, el riesgo global baja a medio.

## Hallazgos Prioritarios (ordenados por severidad)

## 1) Riesgo Critico: Falla de migracion por empleados sin departamento al aplicar no-null

- Impacto: Bloqueo de arranque de aplicacion en ambientes con datos existentes.
- Deteccion: Errores Flyway al aplicar constraint final no-null.
- Causa probable: Backfill incompleto o no deterministico antes de T008.
- Mitigacion requerida:
  - Ejecutar migraciones en orden estricto T005 -> T006 -> T007 -> T008 -> T009.
  - Validar conteo de empleados sin departamento inmediatamente despues de T007.
  - No aplicar T008 hasta que el conteo sea cero.
- Evidencia esperada:
  - Registro en quickstart con query de verificacion y resultado cero.

## 2) Riesgo Critico: Degradacion de seguridad por reglas JWT incompletas en departamentos

- Impacto: Exposicion de endpoints de negocio o respuestas de estado incorrectas.
- Deteccion: Endpoints de departamentos responden 200 sin token o con token invalido.
- Causa probable: Configuracion parcial en SecurityConfig o cadena de filtros no alineada.
- Mitigacion requerida:
  - Aplicar T037 antes de cierre funcional.
  - Ejecutar T038, T039 y T040 como gate de seguridad.
  - Verificar que no exista bypass por Basic en rutas de departamentos.
- Evidencia esperada:
  - Matriz de resultados 401/401/2xx en pruebas de seguridad.

## 3) Riesgo Alto: Conflicto de serializacion (ciclos) entre Empleado y Departamento

- Impacto: Respuestas recursivas, errores de serializacion, payloads excesivos.
- Deteccion: Stacktrace de serializacion o JSON con anidamiento no controlado.
- Causa probable: Exponer entidades JPA directamente o DTOs con referencias bidireccionales.
- Mitigacion requerida:
  - DTOs obligatorios para salida (T022, T034, T036).
  - Prohibir exposicion directa de entidades en controlador.
  - Ejecutar T033 antes de merge.
- Evidencia esperada:
  - Test de serializacion en verde y muestra de payload plano.

## 4) Riesgo Alto: Regresion funcional de empleados por introduccion de departamento obligatorio

- Impacto: Endpoints existentes de empleados fallan por datos o mapeos incompletos.
- Deteccion: Fallo de pruebas de empleados, errores 500 en endpoints existentes.
- Causa probable: Ajustes incompletos en EmpleadoService/EmpleadoController y DTO.
- Mitigacion requerida:
  - Implementar T035 y T049 con prioridad alta.
  - Ejecutar regression suite de iteracion 002 durante Phase 8.
- Evidencia esperada:
  - Pruebas historicas de empleados y autenticacion sin degradacion.

## 5) Riesgo Medio: Unicidad case-insensitive inconsistente entre servicio y base

- Impacto: Duplicados logicos o conflictos intermitentes.
- Deteccion: Casos A/B con mismo nombre en distinto casing.
- Causa probable: Validacion solo en servicio o solo en DB.
- Mitigacion requerida:
  - Mantener doble defensa: validacion de servicio (T028) + constraint/indice DB (T009).
  - Ejecutar T024 y T025 para cubrir ambos niveles.
- Evidencia esperada:
  - 409 consistente para duplicados case-insensitive.

## 6) Riesgo Medio: Contrato OpenAPI desalineado con implementacion real

- Impacto: Pruebas manuales erraticas y consumidores API desactualizados.
- Deteccion: Diferencias entre Swagger y respuestas reales.
- Causa probable: Implementacion sin actualizar contrato/quickstart.
- Mitigacion requerida:
  - Tratar T041-T044 como requisito de cierre, no tarea opcional.
  - Incluir ejemplos de error 409 y autenticacion Bearer.
- Evidencia esperada:
  - Swagger funcional con endpoints y codigos correctos.

## 7) Riesgo Medio: Inestabilidad por orden de ejecucion en tareas paralelas

- Impacto: Retrabajo, conflictos y falsas fallas de integracion.
- Deteccion: Fallas no deterministas entre ramas o PRs.
- Causa probable: Paralelizacion antes de completar prerequisitos fundacionales.
- Mitigacion requerida:
  - No iniciar Phase 3-5 sin cerrar totalmente Phase 2.
  - Aplicar gates por fase y checklist de dependencias.
- Evidencia esperada:
  - Historial de tareas completadas con dependencias respetadas.

## Advertencias Operativas

- No modificar migraciones historicas de iteraciones 001 y 002.
- Evitar rollback manual destructivo en base compartida.
- Mantener pruebas de seguridad activas durante todo el ciclo, no solo al final.
- Registrar evidencia de smoke test dockerizado antes de cerrar la iteracion.

## Checklist de Go/No-Go antes de implementar

1. Backfill definido y verificable para todos los empleados actuales.
2. Politica exacta de departamento por defecto en backfill documentada.
3. SecurityConfig con cobertura explicita de rutas de departamentos bajo JWT.
4. DTO contracts de Empleado y Departamento definidos sin recursividad.
5. Pruebas de conflicto 409 (duplicado y delete con asociados) incluidas.
6. Plan de regresion 002 confirmado en pipeline local.

## Recomendacion Final

Proceder con implementacion por fases como esta en tasks.md, pero con dos gates obligatorios adicionales:

- Gate A (despues de T007): validacion formal de backfill completo.
- Gate B (antes de merge): regression 002 + security 003 + smoke dockerizado en verde.

Con esos gates, el riesgo residual es aceptable para iniciar implementacion incremental.
