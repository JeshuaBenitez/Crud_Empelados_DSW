package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.CreateEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.EmpleadoPageResponse;
import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
import com.dwgabo.dsw02practica01.model.Departamento;
import com.dwgabo.dsw02practica01.model.DepartamentoId;
import com.dwgabo.dsw02practica01.model.Empleado;
import com.dwgabo.dsw02practica01.model.EmpleadoId;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceUs1Test {

    private static final String TEST_EMAIL = "empleado.demo@empresa.com";
    private static final String EMP_PREFIX = "EMP";
    private static final String DEP_PREFIX = "DEP";

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void crearGeneraClaveEmpConsecutiva() {
        CreateEmpleadoRequest request = new CreateEmpleadoRequest();
        request.setNombre("Ana Pérez");
        request.setDireccion("Av. Central 123");
        request.setTelefono("555123456");

        when(empleadoRepository.nextNumero()).thenReturn(1L);
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmpleadoResponse response = empleadoService.crear(request);

        assertEquals("EMP-1", response.getClave());
        assertEquals("Ana Pérez", response.getNombre());

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepository).save(captor.capture());
        Empleado guardado = captor.getValue();
        assertEquals(EMP_PREFIX, guardado.getId().getPrefijo());
        assertEquals(1L, guardado.getId().getNumero());
    }

    @Test
    void listarDevuelvePaginacionConMetadata() {
        Empleado empleado = new Empleado();
        empleado.setId(new EmpleadoId(EMP_PREFIX, 7L));
        empleado.setCorreo(TEST_EMAIL);
        empleado.setNombre("Luis");
        empleado.setDireccion("Calle 7");
        empleado.setTelefono("555777");
        Departamento departamento = new Departamento();
        departamento.setId(new DepartamentoId(DEP_PREFIX, 2L));
        departamento.setNombre("Operaciones");
        empleado.setDepartamento(departamento);

        when(empleadoRepository.findByCorreoIgnoreCase(TEST_EMAIL))
            .thenReturn(Optional.of(empleado));

        EmpleadoPageResponse response = empleadoService.listarPropio(TEST_EMAIL, 0, 10);

        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals("EMP-7", response.getContent().get(0).getClave());
        assertEquals("DEP-2", response.getContent().get(0).getDepartamentoClave());
        assertEquals("Operaciones", response.getContent().get(0).getDepartamentoNombre());
    }

    @Test
    void listarRechazaPaginacionInvalida() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> empleadoService.listarPropio(TEST_EMAIL, -1, 10));
        assertEquals("El parámetro page debe ser mayor o igual a 0", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> empleadoService.listarPropio(TEST_EMAIL, 0, 101));
        assertEquals("El parámetro size debe estar entre 1 y 100", ex2.getMessage());
    }
}
