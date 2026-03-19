package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.CreateEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.EmpleadoPageResponse;
import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
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

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void crear_generaclaveEmpConsecutiva() {
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
        assertEquals("EMP", guardado.getId().getPrefijo());
        assertEquals(1L, guardado.getId().getNumero());
    }

    @Test
    void listar_devuelvePaginacionConMetadata() {
        Empleado empleado = new Empleado();
        empleado.setId(new EmpleadoId("EMP", 7L));
        empleado.setCorreo("empleado.demo@empresa.com");
        empleado.setNombre("Luis");
        empleado.setDireccion("Calle 7");
        empleado.setTelefono("555777");

        when(empleadoRepository.findByCorreoIgnoreCase("empleado.demo@empresa.com"))
            .thenReturn(Optional.of(empleado));

        EmpleadoPageResponse response = empleadoService.listarPropio("empleado.demo@empresa.com", 0, 10);

        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals("EMP-7", response.getContent().get(0).getClave());
    }

    @Test
    void listar_rechazaPaginacionInvalida() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> empleadoService.listarPropio("empleado.demo@empresa.com", -1, 10));
        assertEquals("El parámetro page debe ser mayor o igual a 0", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> empleadoService.listarPropio("empleado.demo@empresa.com", 0, 101));
        assertEquals("El parámetro size debe estar entre 1 y 100", ex2.getMessage());
    }
}
