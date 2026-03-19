CREATE UNIQUE INDEX IF NOT EXISTS uk_departamentos_nombre_lower
    ON departamentos (LOWER(nombre));
