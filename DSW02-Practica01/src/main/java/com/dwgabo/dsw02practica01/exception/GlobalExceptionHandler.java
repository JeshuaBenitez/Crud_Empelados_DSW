package com.dwgabo.dsw02practica01.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DETAILS = "details";

    private static final String MSG_BAD_REQUEST = "Bad Request";
    private static final String MSG_NOT_FOUND = "Not Found";
    private static final String MSG_CONFLICT = "Conflict";
    private static final String MSG_UNAUTHORIZED = "Unauthorized";
    private static final String MSG_FORBIDDEN = "Forbidden";
    private static final String MSG_INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> manejarBadRequest(BadRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(KEY_ERROR, MSG_BAD_REQUEST);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(KEY_ERROR, MSG_BAD_REQUEST);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(KEY_ERROR, MSG_BAD_REQUEST);
        response.put(KEY_MESSAGE, "Parámetro inválido: " + ex.getName());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.NOT_FOUND.value());
        response.put(KEY_ERROR, MSG_NOT_FOUND);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> manejarConflict(ConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.CONFLICT.value());
        response.put(KEY_ERROR, MSG_CONFLICT);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> manejarBadCredentials(BadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.UNAUTHORIZED.value());
        response.put(KEY_ERROR, MSG_UNAUTHORIZED);
        response.put(KEY_MESSAGE, "Credenciales inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> manejarAccessDenied(AccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.FORBIDDEN.value());
        response.put(KEY_ERROR, MSG_FORBIDDEN);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> detalles = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(KEY_ERROR, MSG_BAD_REQUEST);
        response.put(KEY_MESSAGE, "Error de validación");
        response.put(KEY_DETAILS, detalles);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoResource(NoResourceFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.NOT_FOUND.value());
        response.put(KEY_ERROR, MSG_NOT_FOUND);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGenerico(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(KEY_TIMESTAMP, LocalDateTime.now());
        response.put(KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(KEY_ERROR, MSG_INTERNAL_SERVER_ERROR);
        response.put(KEY_MESSAGE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
