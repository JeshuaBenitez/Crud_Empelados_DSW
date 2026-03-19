-- Fix demo account hash introduced in earlier seed iterations.
-- Password for demo account remains: Empleado123!
UPDATE empleados
SET password_hash = '$2a$10$JVaaRJEIoZJY9LgfZTN/H.A7DReUhUVHBaleB26l0bVkrTzxUMUlG'
WHERE LOWER(correo) = 'empleado.demo@empresa.com';

-- Also normalize rows that still have the previous incorrect hash value.
UPDATE empleados
SET password_hash = '$2a$10$JVaaRJEIoZJY9LgfZTN/H.A7DReUhUVHBaleB26l0bVkrTzxUMUlG'
WHERE password_hash = '$2a$10$7A4ot9l6TF3M7n3y9jY/7u2RRa8L9sj1Y8rkM4vM0I7P8RZ8o4VOu';
