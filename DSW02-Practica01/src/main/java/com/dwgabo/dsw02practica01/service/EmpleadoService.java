package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.CreateEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.EmpleadoPageResponse;
import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
import com.dwgabo.dsw02practica01.dto.UpdateEmpleadoRequest;
import com.dwgabo.dsw02practica01.exception.ConflictException;
import com.dwgabo.dsw02practica01.exception.ResourceNotFoundException;
import com.dwgabo.dsw02practica01.model.Empleado;
import com.dwgabo.dsw02practica01.model.EmpleadoId;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmpleadoService {

    private static final String PREFIJO = "EMP";
    private static final Pattern CLAVE_PATTERN = Pattern.compile("^EMP-(\\d+)$");
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public EmpleadoPageResponse listar(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("El parámetro page debe ser mayor o igual a 0");
        }
        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException("El parámetro size debe estar entre 1 y 100");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Empleado> empleados = empleadoRepository.findAll(pageable);

        EmpleadoPageResponse response = new EmpleadoPageResponse();
        response.setContent(empleados.getContent().stream().map(this::toResponse).toList());
        response.setPage(empleados.getNumber());
        response.setSize(empleados.getSize());
        response.setTotalElements(empleados.getTotalElements());
        response.setTotalPages(empleados.getTotalPages());
        return response;
    }

    public EmpleadoPageResponse listarPorDefecto() {
        return listar(DEFAULT_PAGE, DEFAULT_SIZE);
    }

    public EmpleadoResponse obtenerPorClave(String clave) {
        EmpleadoId id = parseClave(clave);
        return toResponse(obtenerPorId(id, clave));
    }

    public EmpleadoResponse crear(CreateEmpleadoRequest request) {
        Long numero = empleadoRepository.nextNumero();
        if (numero == null) {
            throw new IllegalStateException("No fue posible generar la clave de empleado");
        }

        Empleado empleado = new Empleado();
        empleado.setId(new EmpleadoId(PREFIJO, numero));
        empleado.setNombre(request.getNombre());
        empleado.setDireccion(request.getDireccion());
        empleado.setTelefono(request.getTelefono());

        try {
            return toResponse(empleadoRepository.save(empleado));
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("La clave compuesta del empleado ya existe");
        }
    }

    public EmpleadoResponse actualizar(String clave, UpdateEmpleadoRequest empleadoActualizado) {
        EmpleadoId id = parseClave(clave);
        Empleado existente = obtenerPorId(id, clave);
        existente.setNombre(empleadoActualizado.getNombre());
        existente.setDireccion(empleadoActualizado.getDireccion());
        existente.setTelefono(empleadoActualizado.getTelefono());
        return toResponse(empleadoRepository.save(existente));
    }

    public void eliminar(String clave) {
        EmpleadoId id = parseClave(clave);
        Empleado existente = obtenerPorId(id, clave);
        empleadoRepository.delete(existente);
    }

    private Empleado obtenerPorId(EmpleadoId id, String clave) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con clave: " + clave));
    }

    private EmpleadoId parseClave(String clave) {
        Matcher matcher = CLAVE_PATTERN.matcher(clave);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de clave inválido. Debe ser EMP-<numero>");
        }
        Long numero = Long.parseLong(matcher.group(1));
        return new EmpleadoId(PREFIJO, numero);
    }

    private EmpleadoResponse toResponse(Empleado empleado) {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setClave(empleado.getClave());
        response.setNombre(empleado.getNombre());
        response.setDireccion(empleado.getDireccion());
        response.setTelefono(empleado.getTelefono());
        return response;
    }
}
