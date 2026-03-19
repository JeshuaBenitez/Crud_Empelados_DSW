package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.CreateDepartamentoRequest;
import com.dwgabo.dsw02practica01.dto.DepartamentoPageResponse;
import com.dwgabo.dsw02practica01.dto.DepartamentoResponse;
import com.dwgabo.dsw02practica01.dto.UpdateDepartamentoRequest;
import com.dwgabo.dsw02practica01.exception.ConflictException;
import com.dwgabo.dsw02practica01.exception.ResourceNotFoundException;
import com.dwgabo.dsw02practica01.model.Departamento;
import com.dwgabo.dsw02practica01.model.DepartamentoId;
import com.dwgabo.dsw02practica01.repository.DepartamentoRepository;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DepartamentoService {

    private static final String PREFIJO = "DEP";
    private static final Pattern CLAVE_PATTERN = Pattern.compile("^DEP-(\\d+)$");
    private static final int MAX_SIZE = 100;

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               EmpleadoRepository empleadoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public DepartamentoResponse crear(CreateDepartamentoRequest request) {
        if (departamentoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ConflictException("Ya existe un departamento con ese nombre");
        }

        Long numero = departamentoRepository.nextNumero();
        if (numero == null) {
            throw new IllegalStateException("No fue posible generar la clave de departamento");
        }

        Departamento departamento = new Departamento();
        departamento.setId(new DepartamentoId(PREFIJO, numero));
        departamento.setNombre(request.getNombre());

        return toResponse(departamentoRepository.save(departamento));
    }

    public DepartamentoPageResponse listar(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("El parámetro page debe ser mayor o igual a 0");
        }
        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException("El parámetro size debe estar entre 1 y 100");
        }

        Page<Departamento> departamentos = departamentoRepository.findAll(PageRequest.of(page, size));
        DepartamentoPageResponse response = new DepartamentoPageResponse();
        response.setContent(departamentos.getContent().stream().map(this::toResponse).toList());
        response.setPage(departamentos.getNumber());
        response.setSize(departamentos.getSize());
        response.setTotalElements(departamentos.getTotalElements());
        response.setTotalPages(departamentos.getTotalPages());
        return response;
    }

    public DepartamentoResponse obtenerPorClave(String clave) {
        DepartamentoId id = parseClave(clave);
        Departamento departamento = obtenerPorId(id, clave);
        return toResponse(departamento);
    }

    public DepartamentoResponse actualizar(String clave, UpdateDepartamentoRequest request) {
        DepartamentoId id = parseClave(clave);
        Departamento existente = obtenerPorId(id, clave);

        if (departamentoRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre(), id)) {
            throw new ConflictException("Ya existe un departamento con ese nombre");
        }

        existente.setNombre(request.getNombre());
        return toResponse(departamentoRepository.save(existente));
    }

    public void eliminar(String clave) {
        DepartamentoId id = parseClave(clave);
        Departamento existente = obtenerPorId(id, clave);

        if (empleadoRepository.existsByDepartamento(id.getPrefijo(), id.getNumero())) {
            throw new ConflictException("No se puede eliminar el departamento porque tiene empleados asociados");
        }

        departamentoRepository.delete(existente);
    }

    private Departamento obtenerPorId(DepartamentoId id, String clave) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento no encontrado con clave: " + clave));
    }

    private DepartamentoId parseClave(String clave) {
        Matcher matcher = CLAVE_PATTERN.matcher(clave);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de clave inválido. Debe ser DEP-<numero>");
        }
        Long numero = Long.parseLong(matcher.group(1));
        return new DepartamentoId(PREFIJO, numero);
    }

    private DepartamentoResponse toResponse(Departamento departamento) {
        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave(departamento.getClave());
        response.setNombre(departamento.getNombre());
        return response;
    }
}
