package com.eventos.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Error de validación");
        response.put("errores", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // Errores de recurso no encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Recurso no encontrado");
        response.put("mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Errores de lógica de negocio (conflictos, duplicados, validaciones)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        String mensaje = ex.getMessage();

        // Determinar el tipo de error según el mensaje
        HttpStatus status;
        String errorType;

        if (mensaje.contains("no encontrado") || mensaje.contains("No se encontró")) {
            status = HttpStatus.NOT_FOUND;
            errorType = "Recurso no encontrado";
        } else if (mensaje.contains("Ya existe") || mensaje.contains("duplicado") ||
                mensaje.contains("ya está") || mensaje.contains("ya ha")) {
            status = HttpStatus.CONFLICT;
            errorType = "Conflicto de datos";
        } else if (mensaje.contains("debe") || mensaje.contains("no puede") ||
                mensaje.contains("Solo se puede") || mensaje.contains("capacidad")) {
            status = HttpStatus.BAD_REQUEST;
            errorType = "Regla de negocio no cumplida";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorType = "Error del servidor";
        }

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", errorType);
        response.put("mensaje", mensaje);

        return ResponseEntity.status(status).body(response);
    }

    // Manejo de errores internos del servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error interno del servidor");
        response.put("mensaje", ex.getMessage());
        response.put("tipo", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}