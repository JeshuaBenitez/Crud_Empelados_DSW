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
@SuppressWarnings("java:S2068")
class AuthServiceUs1Test {

    private static final String TEST_EMAIL = "empleado.demo@empresa.com";
    private static final String TEST_PASSWORD = "Empleado123!";
    private static final String INVALID_PASSWORD = "wrong";
    private static final String HASHED_PASSWORD = "hashed";
    private static final String TOKEN_VALUE = "token-demo";
    private static final String TOKEN_TYPE_BEARER = "Bearer";

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginExitosoDevuelveToken() {
        Empleado empleado = new Empleado();
        empleado.setId(new EmpleadoId("EMP", 1L));
        empleado.setCorreo(TEST_EMAIL);
        empleado.setPasswordHash(HASHED_PASSWORD);
        empleado.setActivo(true);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo(TEST_EMAIL);
        request.setPassword(TEST_PASSWORD);

        when(empleadoRepository.findByCorreoIgnoreCase(TEST_EMAIL)).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString())).thenReturn(TOKEN_VALUE);
        when(jwtTokenProvider.getExpirationMs()).thenReturn(3600000L);

        LoginEmpleadoResponse response = authService.login(request);

        assertEquals(TOKEN_VALUE, response.getAccessToken());
        assertEquals(TOKEN_TYPE_BEARER, response.getTokenType());
        assertEquals(3600000L, response.getExpiresIn());
    }

    @Test
    void loginConPasswordInvalidoLanzaUnauthorized() {
        Empleado empleado = new Empleado();
        empleado.setCorreo(TEST_EMAIL);
        empleado.setPasswordHash(HASHED_PASSWORD);
        empleado.setActivo(true);

        LoginEmpleadoRequest request = new LoginEmpleadoRequest();
        request.setCorreo(TEST_EMAIL);
        request.setPassword(INVALID_PASSWORD);

        when(empleadoRepository.findByCorreoIgnoreCase(TEST_EMAIL)).thenReturn(Optional.of(empleado));
        when(passwordEncoder.matches(INVALID_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}
