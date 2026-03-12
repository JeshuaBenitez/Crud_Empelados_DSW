# Quickstart: CRUD de Empleados

## Prerrequisitos

- Java 17
- Maven 3.9+
- Docker y Docker Compose

## 1) Levantar PostgreSQL con Docker

Desde `DSW02-Practica01/`:

```bash
docker compose up -d
```

## 2) Ejecutar la aplicación

Desde `DSW02-Practica01/`:

```bash
export APP_DB_USER=postgres
export APP_DB_PASSWORD=postgres
export APP_BASIC_USER=admin
export APP_BASIC_PASSWORD=admin123
mvn spring-boot:run
```

## 3) Probar Swagger

- URL: `http://localhost:8080/swagger-ui.html`
- Usuario Basic Auth: `admin`
- Password Basic Auth: `admin123`

## 4) Probar endpoints principales

Base URL: `http://localhost:8080/api/v1/empleados`

### Crear empleado

```bash
curl -u "admin:admin123" -X POST "http://localhost:8080/api/v1/empleados" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Pérez","direccion":"Av. Central 123","telefono":"555123456"}'
```

### Listar empleados

```bash
curl -u "admin:admin123" "http://localhost:8080/api/v1/empleados?page=0&size=10"
```

### Obtener por clave

```bash
curl -u "admin:admin123" "http://localhost:8080/api/v1/empleados/EMP-1"
```

### Actualizar

```bash
curl -u "admin:admin123" -X PUT "http://localhost:8080/api/v1/empleados/EMP-1" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Gómez","direccion":"Calle 45","telefono":"555000111"}'
```

### Eliminar

```bash
curl -u "admin:admin123" -X DELETE "http://localhost:8080/api/v1/empleados/EMP-1"
```
