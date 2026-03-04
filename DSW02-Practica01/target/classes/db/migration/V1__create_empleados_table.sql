CREATE SEQUENCE IF NOT EXISTS empleados_numero_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS empleados (
    prefijo VARCHAR(3) NOT NULL DEFAULT 'EMP',
    numero BIGINT NOT NULL DEFAULT nextval('empleados_numero_seq'),
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    telefono VARCHAR(100) NOT NULL,
    CONSTRAINT pk_empleados PRIMARY KEY (prefijo, numero),
    CONSTRAINT chk_empleados_prefijo CHECK (prefijo = 'EMP')
);
