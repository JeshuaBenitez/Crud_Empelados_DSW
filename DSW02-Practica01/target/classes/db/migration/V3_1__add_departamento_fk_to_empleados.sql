ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS departamento_prefijo VARCHAR(3),
    ADD COLUMN IF NOT EXISTS departamento_numero BIGINT;

ALTER TABLE empleados
    DROP CONSTRAINT IF EXISTS fk_empleados_departamentos;

ALTER TABLE empleados
    ADD CONSTRAINT fk_empleados_departamentos
        FOREIGN KEY (departamento_prefijo, departamento_numero)
        REFERENCES departamentos (prefijo, numero);
