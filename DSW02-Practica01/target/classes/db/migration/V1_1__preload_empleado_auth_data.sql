CREATE TABLE IF NOT EXISTS empleados_auth_preload_audit (
    id BIGSERIAL PRIMARY KEY,
    executed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    empleados_count BIGINT NOT NULL
);

INSERT INTO empleados_auth_preload_audit (empleados_count)
SELECT COUNT(*) FROM empleados;
