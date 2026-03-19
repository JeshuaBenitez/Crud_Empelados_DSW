package com.dwgabo.dsw02practica01.service;

import com.dwgabo.dsw02practica01.dto.LoginEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.LoginEmpleadoResponse;
import com.dwgabo.dsw02practica01.model.Empleado;
import com.dwgabo.dsw02practica01.repository.EmpleadoRepository;
import com.dwgabo.dsw02practica01.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(EmpleadoRepository empleadoRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginEmpleadoResponse login(LoginEmpleadoRequest request) {
        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase(request.getCorreo())
                .orElseThrow(this::invalidCredentials);

        if (!Boolean.TRUE.equals(empleado.getActivo())) {
            throw invalidCredentials();
        }

        if (!passwordEncoder.matches(request.getPassword(), empleado.getPasswordHash())) {
            throw invalidCredentials();
        }

        LoginEmpleadoResponse response = new LoginEmpleadoResponse();
        response.setAccessToken(jwtTokenProvider.generateToken(empleado.getCorreo(), empleado.getClave()));
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtTokenProvider.getExpirationMs());
        return response;
    }

    private BadCredentialsException invalidCredentials() {
        return new BadCredentialsException("Credenciales inválidas");
    }
}
