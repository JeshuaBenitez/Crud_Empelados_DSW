INSERT INTO departamentos (prefijo, numero, nombre)
SELECT 'DEP', 1, 'General'
WHERE NOT EXISTS (
    SELECT 1 FROM departamentos WHERE prefijo = 'DEP' AND numero = 1
);

SELECT setval('departamentos_numero_seq', GREATEST((SELECT COALESCE(MAX(numero), 1) FROM departamentos), 1), true);

UPDATE empleados
SET departamento_prefijo = 'DEP',
    departamento_numero = 1
WHERE departamento_prefijo IS NULL
   OR departamento_numero IS NULL;
