package com.example.api.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo global de errores de la API. Devuelve siempre un JSON con los campos
 * {@code timestamp}, {@code status} y {@code error}. Nunca incluye contraseñas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Recurso inexistente (producto o usuario).
     *
     * @param ex excepción lanzada por el servicio
     * @return respuesta HTTP 404 con el mensaje de error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    /**
     * Regla de negocio incumplida (por ejemplo, correo duplicado).
     *
     * @param ex excepción lanzada por el servicio
     * @return respuesta HTTP 400 con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    /**
     * Fallo de validación del cuerpo de la petición ({@code @Valid}).
     *
     * @param ex excepción con los errores de campo
     * @return respuesta HTTP 400 con el detalle por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> details.putIfAbsent(fe.getField(), fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos", details);
    }

    /**
     * JSON mal formado o con tipos incorrectos, o identificador no numérico en la ruta.
     *
     * @param ex excepción lanzada por Spring al leer la petición
     * @return respuesta HTTP 400
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleUnreadable(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Petición mal formada: revise el JSON y los tipos de datos", null);
    }

    /**
     * Violación de integridad en base de datos (por ejemplo, correo duplicado por concurrencia).
     *
     * @param ex excepción de Spring Data
     * @return respuesta HTTP 409 con mensaje genérico (sin datos sensibles)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "La operación viola una restricción de integridad de datos", null);
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", message);
        if (details != null) {
            body.put("detalles", details);
        }
        return ResponseEntity.status(status).body(body);
    }
}
