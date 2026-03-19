package com.dwgabo.dsw02practica01.controller;

import com.dwgabo.dsw02practica01.dto.CreateDepartamentoRequest;
import com.dwgabo.dsw02practica01.dto.DepartamentoPageResponse;
import com.dwgabo.dsw02practica01.dto.DepartamentoResponse;
import com.dwgabo.dsw02practica01.dto.UpdateDepartamentoRequest;
import com.dwgabo.dsw02practica01.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody CreateDepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<DepartamentoPageResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(departamentoService.listar(page, size));
    }

    @GetMapping("/{clave}")
    public ResponseEntity<DepartamentoResponse> obtener(@PathVariable String clave) {
        return ResponseEntity.ok(departamentoService.obtenerPorClave(clave));
    }

    @PutMapping("/{clave}")
    public ResponseEntity<DepartamentoResponse> actualizar(@PathVariable String clave,
                                                           @Valid @RequestBody UpdateDepartamentoRequest request) {
        return ResponseEntity.ok(departamentoService.actualizar(clave, request));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        departamentoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
