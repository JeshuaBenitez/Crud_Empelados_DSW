package com.dwgabo.dsw02practica01.repository;

import com.dwgabo.dsw02practica01.model.Empleado;
import com.dwgabo.dsw02practica01.model.EmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {

	@Query(value = "SELECT nextval('empleados_numero_seq')", nativeQuery = true)
	Long nextNumero();

	Optional<Empleado> findByCorreoIgnoreCase(String correo);
}
