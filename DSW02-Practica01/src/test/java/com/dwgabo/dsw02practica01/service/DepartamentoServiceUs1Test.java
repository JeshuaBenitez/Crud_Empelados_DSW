package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.CreateDepartamentoRequest;
import com.dwgabo.dsw02practica01.dto.DepartamentoResponse;
import com.dwgabo.dsw02practica01.exception.ConflictException;
import com.dwgabo.dsw02practica01.model.Departamento;
import com.dwgabo.dsw02practica01.model.DepartamentoId;
import com.dwgabo.dsw02practica01.repository.DepartamentoRepository;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartamentoServiceUs1Test {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private DepartamentoService departamentoService;

    @Test
    void crear_departamentoConClaveDep() {
        CreateDepartamentoRequest request = new CreateDepartamentoRequest();
        request.setNombre("Recursos Humanos");

        when(departamentoRepository.existsByNombreIgnoreCase("Recursos Humanos")).thenReturn(false);
        when(departamentoRepository.nextNumero()).thenReturn(1L);
        when(departamentoRepository.save(any(Departamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartamentoResponse response = departamentoService.crear(request);

        assertEquals("DEP-1", response.getClave());
        assertEquals("Recursos Humanos", response.getNombre());
    }

    @Test
    void crear_nombreDuplicadoLanzaConflict() {
        CreateDepartamentoRequest request = new CreateDepartamentoRequest();
        request.setNombre("Operacion");

        when(departamentoRepository.existsByNombreIgnoreCase("Operacion")).thenReturn(true);

        assertThrows(ConflictException.class, () -> departamentoService.crear(request));
    }

    @Test
    void eliminar_conEmpleadosAsociadosLanzaConflict() {
        Departamento departamento = new Departamento();
        departamento.setId(new DepartamentoId("DEP", 1L));
        departamento.setNombre("Operacion");

        when(departamentoRepository.findById(new DepartamentoId("DEP", 1L))).thenReturn(java.util.Optional.of(departamento));
        when(empleadoRepository.existsByDepartamento("DEP", 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> departamentoService.eliminar("DEP-1"));
    }
}
