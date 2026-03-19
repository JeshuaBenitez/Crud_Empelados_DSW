package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.dto.LoginEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.LoginEmpleadoResponse;
import com.dwgabo.dsw02practica01.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/empleados")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginEmpleadoResponse> login(@Valid @RequestBody LoginEmpleadoRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
