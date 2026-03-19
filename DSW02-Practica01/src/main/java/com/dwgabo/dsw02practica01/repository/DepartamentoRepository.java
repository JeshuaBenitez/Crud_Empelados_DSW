package com.dwgabo.dsw02practica01.repository;

import com.dwgabo.dsw02practica01.model.Departamento;
import com.dwgabo.dsw02practica01.model.DepartamentoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartamentoRepository extends JpaRepository<Departamento, DepartamentoId> {

    @Query(value = "SELECT nextval('departamentos_numero_seq')", nativeQuery = true)
    Long nextNumero();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, DepartamentoId id);
}
