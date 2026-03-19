-- Populate auth fields for existing rows before enforcing non-null constraints.
UPDATE empleados
SET correo = LOWER('emp' || numero || '@example.local')
WHERE correo IS NULL;

-- BCrypt hash for password: Empleado123!
UPDATE empleados
SET password_hash = '$2a$10$7A4ot9l6TF3M7n3y9jY/7u2RRa8L9sj1Y8rkM4vM0I7P8RZ8o4VOu'
WHERE password_hash IS NULL;

UPDATE empleados
SET activo = TRUE
WHERE activo IS NULL;

-- Ensure at least one deterministic account exists for local login tests.
INSERT INTO empleados (nombre, direccion, telefono, correo, password_hash, activo)
SELECT 'Empleado Demo', 'Direccion Demo 123', '555000111', 'empleado.demo@empresa.com',
       '$2a$10$7A4ot9l6TF3M7n3y9jY/7u2RRa8L9sj1Y8rkM4vM0I7P8RZ8o4VOu', TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM empleados WHERE LOWER(correo) = 'empleado.demo@empresa.com'
);

ALTER TABLE empleados
    ALTER COLUMN correo SET NOT NULL,
    ALTER COLUMN password_hash SET NOT NULL,
    ALTER COLUMN activo SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_empleados_correo_lower ON empleados (LOWER(correo));
