package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.dto.CreateEmpleadoRequest;
import com.dwgabo.dsw02practica01.dto.EmpleadoPageResponse;
import com.dwgabo.dsw02practica01.dto.EmpleadoResponse;
import com.dwgabo.dsw02practica01.dto.UpdateEmpleadoRequest;
import com.dwgabo.dsw02practica01.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<EmpleadoPageResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return ResponseEntity.ok(empleadoService.listarPropio(authentication.getName(), page, size));
    }

    @GetMapping("/{clave}")
    public ResponseEntity<EmpleadoResponse> obtener(@PathVariable String clave, Authentication authentication) {
        return ResponseEntity.ok(empleadoService.obtenerPorClave(clave, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody CreateEmpleadoRequest empleado) {
        throw new AccessDeniedException("No tienes permiso para crear empleados desde esta ruta");
    }

    @PutMapping("/{clave}")
    public ResponseEntity<EmpleadoResponse> actualizar(@PathVariable String clave,
                                                       @Valid @RequestBody UpdateEmpleadoRequest empleado,
                                                       Authentication authentication) {
        return ResponseEntity.ok(empleadoService.actualizar(clave, empleado, authentication.getName()));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave, Authentication authentication) {
        empleadoService.eliminar(clave, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
