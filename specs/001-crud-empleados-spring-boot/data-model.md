# Data Model: CRUD de Empleados

## Entity: Empleado

### Fields

- `prefijo` (String, parte de PK compuesta, valor fijo `EMP`)
- `numero` (Long, parte de PK compuesta, autonumérico)
- `clave` (String lógico derivado: `EMP-<numero>`, expuesto en API)
- `nombre` (String, requerido, máximo 100 caracteres)
- `direccion` (String, requerido, máximo 100 caracteres)
- `telefono` (String, requerido, máximo 100 caracteres)

### Constraints

1. (`prefijo`, `numero`) debe ser una combinación única y no nula.
2. `prefijo` debe ser siempre `EMP`.
3. `numero` se genera automáticamente.
4. `nombre`, `direccion`, `telefono` no deben ser nulos ni vacíos.
5. `nombre`, `direccion`, `telefono` deben tener longitud <= 100.

### Persistence Mapping

- Tabla: `empleados`
- Primary Key: compuesta por (`prefijo`, `numero`)
- Columnas:
  - `prefijo VARCHAR(3) NOT NULL DEFAULT 'EMP'`
  - `numero BIGINT NOT NULL DEFAULT nextval('empleados_numero_seq')`
  - `nombre VARCHAR(100) NOT NULL`
  - `direccion VARCHAR(100) NOT NULL`
  - `telefono VARCHAR(100) NOT NULL`

## Error Model (API)

- `400 Bad Request`: payload inválido o violaciones de validación de campos.
- `401 Unauthorized`: credenciales Basic Auth faltantes o inválidas.
- `404 Not Found`: empleado inexistente en operaciones por `clave` (`EMP-<numero>`).
- `409 Conflict`: intento de crear empleado con combinación de PK compuesta duplicada.

## API Access & Query Model

- Versionado obligatorio de rutas: `/api/v1/empleados`.
- Seguridad de acceso: HTTP Basic con credenciales iniciales `admin` / `admin123`.
- Listado paginado:
  - Parámetros: `page` (entero >= 0), `size` (entero > 0).
  - Comportamiento esperado: respuesta paginada para evitar listados masivos sin control.
