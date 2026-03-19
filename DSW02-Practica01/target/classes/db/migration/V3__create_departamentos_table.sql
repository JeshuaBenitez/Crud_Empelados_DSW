CREATE SEQUENCE IF NOT EXISTS departamentos_numero_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS departamentos (
    prefijo VARCHAR(3) NOT NULL DEFAULT 'DEP',
    numero BIGINT NOT NULL DEFAULT nextval('departamentos_numero_seq'),
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT pk_departamentos PRIMARY KEY (prefijo, numero),
    CONSTRAINT chk_departamentos_prefijo CHECK (prefijo = 'DEP')
);
