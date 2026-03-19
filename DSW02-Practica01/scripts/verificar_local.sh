#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

APP_DB_HOST="${APP_DB_HOST:-localhost}"
APP_DB_PORT="${APP_DB_PORT:-5433}"
APP_DB_NAME="${APP_DB_NAME:-empleados_db}"
APP_DB_USER="${APP_DB_USER:-postgres}"
APP_DB_PASSWORD="${APP_DB_PASSWORD:-postgres}"
APP_BASIC_USER="${APP_BASIC_USER:-admin}"
APP_BASIC_PASSWORD="${APP_BASIC_PASSWORD:-admin123}"
APP_JWT_SECRET="${APP_JWT_SECRET:-jwt-secret-change-me-to-at-least-32-bytes-key}"
APP_JWT_EXPIRATION_MS="${APP_JWT_EXPIRATION_MS:-3600000}"
BASE_URL="${BASE_URL:-http://localhost:8080}"

APP_PID=""

cleanup() {
  if [[ -n "$APP_PID" ]] && ps -p "$APP_PID" >/dev/null 2>&1; then
    echo "[INFO] Deteniendo app Spring Boot (PID $APP_PID)..."
    kill "$APP_PID" >/dev/null 2>&1 || true
    wait "$APP_PID" 2>/dev/null || true
  fi
}
trap cleanup EXIT

cd "$PROJECT_DIR"

echo "[1/5] Levantando PostgreSQL en Docker..."
docker compose up -d postgres >/dev/null

echo "[2/5] Ejecutando pruebas Maven..."
mvn test

echo "[3/5] Arrancando API en segundo plano..."
LOG_FILE="$(mktemp)"
APP_DB_HOST="$APP_DB_HOST" \
APP_DB_PORT="$APP_DB_PORT" \
APP_DB_NAME="$APP_DB_NAME" \
APP_DB_USER="$APP_DB_USER" \
APP_DB_PASSWORD="$APP_DB_PASSWORD" \
APP_BASIC_USER="$APP_BASIC_USER" \
APP_BASIC_PASSWORD="$APP_BASIC_PASSWORD" \
APP_JWT_SECRET="$APP_JWT_SECRET" \
APP_JWT_EXPIRATION_MS="$APP_JWT_EXPIRATION_MS" \
mvn spring-boot:run >"$LOG_FILE" 2>&1 &
APP_PID=$!

echo "[4/5] Esperando disponibilidad de la API..."
READY=0
for _ in {1..60}; do
  CODE=$(curl -s -o /dev/null -w '%{http_code}' "$BASE_URL/swagger-ui.html" || true)
  if [[ "$CODE" == "200" || "$CODE" == "302" ]]; then
    READY=1
    break
  fi
  sleep 1
done

if [[ "$READY" != "1" ]]; then
  echo "[ERROR] La API no respondió a tiempo. Últimas líneas de log:"
  tail -n 80 "$LOG_FILE" || true
  exit 1
fi

echo "[5/5] Ejecutando smoke tests..."
UNAUTH_CODE=$(curl -s -o /tmp/empleados_unauth.out -w '%{http_code}' "$BASE_URL/api/v1/empleados")
LOGIN_BODY=$(curl -s -X POST "$BASE_URL/api/v1/auth/empleados/login" \
  -H 'Content-Type: application/json' \
  -d '{"correo":"empleado.demo@empresa.com","password":"Empleado123!"}')
TOKEN=$(echo "$LOGIN_BODY" | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p')
AUTH_CODE=$(curl -s -o /tmp/empleados_auth.out -w '%{http_code}' "$BASE_URL/api/v1/empleados" -H "Authorization: Bearer $TOKEN")
INVALID_CODE=$(curl -s -o /tmp/empleados_invalid.out -w '%{http_code}' "$BASE_URL/api/v1/empleados" -H 'Authorization: Bearer token-invalido')
LOGIN_EMPTY_CODE=$(curl -s -o /tmp/login_empty.out -w '%{http_code}' -X POST "$BASE_URL/api/v1/auth/empleados/login" -H 'Content-Type: application/json' -d '{}')

FAIL=0

if [[ "$UNAUTH_CODE" != "401" ]]; then
  echo "[ERROR] Esperado UNAUTH=401, obtenido $UNAUTH_CODE"
  FAIL=1
fi

if [[ -z "$TOKEN" ]]; then
  echo "[ERROR] No se obtuvo accessToken en login. Respuesta: $LOGIN_BODY"
  FAIL=1
fi

if [[ "$AUTH_CODE" != "200" ]]; then
  echo "[ERROR] Esperado AUTH=200, obtenido $AUTH_CODE"
  FAIL=1
fi

if [[ "$INVALID_CODE" != "401" ]]; then
  echo "[ERROR] Esperado INVALID=401, obtenido $INVALID_CODE"
  FAIL=1
fi

if [[ "$LOGIN_EMPTY_CODE" != "400" ]]; then
  echo "[ERROR] Esperado LOGIN_EMPTY=400, obtenido $LOGIN_EMPTY_CODE"
  FAIL=1
fi

echo ""
echo "Resumen smoke test"
echo "- UNAUTH_CODE=$UNAUTH_CODE"
echo "- TOKEN_LEN=${#TOKEN}"
echo "- AUTH_CODE=$AUTH_CODE"
echo "- INVALID_CODE=$INVALID_CODE"
echo "- LOGIN_EMPTY_CODE=$LOGIN_EMPTY_CODE"

if [[ "$FAIL" == "1" ]]; then
  echo ""
  echo "[ERROR] Verificación completada con fallos"
  tail -n 60 "$LOG_FILE" || true
  exit 1
fi

echo ""
echo "[OK] Verificación completada correctamente"
