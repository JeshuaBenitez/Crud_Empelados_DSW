# Quickstart: Iteracion 003 (Departamentos)

## Requisitos

- Docker y Docker Compose
- Java 17
- Maven

## Levantar entorno

```bash
cd /home/jbenitez/DW_Gabo/DSW02-Practica01
docker compose up -d postgres
mvn spring-boot:run
```

## Login JWT

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/empleados/login \
  -H 'Content-Type: application/json' \
  -d '{"correo":"empleado.demo@empresa.com","password":"Empleado123!"}'
```

Tomar `accessToken` y usarlo como Bearer.

## CRUD de departamentos

### Crear

```bash
curl -i -X POST http://localhost:8080/api/v1/departamentos \
  -H "Authorization: Bearer <TOKEN>" \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Recursos Humanos"}'
```

### Listar paginado

```bash
curl -i "http://localhost:8080/api/v1/departamentos?page=0&size=10" \
  -H "Authorization: Bearer <TOKEN>"
```

### Obtener por clave

```bash
curl -i http://localhost:8080/api/v1/departamentos/DEP-1 \
  -H "Authorization: Bearer <TOKEN>"
```

### Actualizar

```bash
curl -i -X PUT http://localhost:8080/api/v1/departamentos/DEP-1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Operacion"}'
```

### Eliminar

```bash
curl -i -X DELETE http://localhost:8080/api/v1/departamentos/DEP-1 \
  -H "Authorization: Bearer <TOKEN>"
```

## Validaciones clave

- Nombre duplicado (case-insensitive): `409 Conflict`.
- Eliminar departamento con empleados asociados: `409 Conflict`.
- Sin token o token invalido: `401 Unauthorized`.

## Validacion de relacion con empleados

Al consultar empleados, la respuesta incluye referencia plana de departamento:

- `departamentoClave`
- `departamentoNombre`

Esto evita ciclos de serializacion en JSON.
