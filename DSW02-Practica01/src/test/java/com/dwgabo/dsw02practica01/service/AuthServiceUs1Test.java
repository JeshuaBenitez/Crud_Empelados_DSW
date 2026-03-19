package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.LoginEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.LoginEmpleadoResponse;
import com.dwgabo.dsw02practica01.model.Empleado;
import com.dwgabo.dsw02practica01.model.EmpleadoId;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceUs1Test {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginExitoso_devuelveToken() {
        Empleado empleado = new Empleado();
        empleado.setId(new EmpleadoId("EMP", 1L));
        empleado.setCorreo("empleado.demo@empresa.com");
        empleado.setPasswordHash("hashed");
        empleado.setActivo(true);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo("empleado.demo@empresa.com");
        request.setPassword("Empleado123!");

        when(empleadoRepository.findByCorreoIgnoreCase("empleado.demo@empresa.com")).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches("Empleado123!", "hashed")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn("token-demo");
        when(jwtTokenProvider.getExpirationMs()).thenReturn(3600000L);

        LoginEmpleadoResponse response = authService.login(request);

        assertEquals("token-demo", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600000L, response.getExpiresIn());
    }

    @Test
    void loginConPasswordInvalido_lanzaUnauthorized() {
        Empleado empleado = new Empleado();
        empleado.setCorreo("empleado.demo@empresa.com");
        empleado.setPasswordHash("hashed");
        empleado.setActivo(true);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo("empleado.demo@empresa.com");
        request.setPassword("wrong");

        when(empleadoRepository.findByCorreoIgnoreCase("empleado.demo@empresa.com")).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
