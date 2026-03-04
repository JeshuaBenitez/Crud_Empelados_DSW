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
export APP_BASIC_PASSWORD='tu-password-segura'
mvn spring-boot:run
```

## 3) Probar Swagger

- URL: `http://localhost:8080/swagger-ui.html`
- Usuario Basic Auth: valor de `APP_BASIC_USER`
- Password Basic Auth: valor de `APP_BASIC_PASSWORD`

## 4) Probar endpoints principales

Base URL: `http://localhost:8080/api/empleados`

### Crear empleado

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" -X POST "http://localhost:8080/api/empleados" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Pérez","direccion":"Av. Central 123","telefono":"555123456"}'
```

### Listar empleados

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" "http://localhost:8080/api/empleados"
```

### Obtener por clave

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" "http://localhost:8080/api/empleados/EMP-1"
```

### Actualizar

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" -X PUT "http://localhost:8080/api/empleados/EMP-1" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana Gómez","direccion":"Calle 45","telefono":"555000111"}'
```

### Eliminar

```bash
curl -u "$APP_BASIC_USER:$APP_BASIC_PASSWORD" -X DELETE "http://localhost:8080/api/empleados/EMP-1"
```
