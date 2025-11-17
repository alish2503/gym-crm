package com.gymcrm.presentation.advice;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Alish
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("error", "Entity not found");
        response.put("message", ex.getMessage() != null ? ex.getMessage() : "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        f -> Objects.requireNonNullElse(f.getDefaultMessage(), ""),
                        (existing, replacement) -> existing
                ));

        log.error("Validation failed: {}", fieldErrors);
        Map<String, String> response = new LinkedHashMap<>();
        response.put("error", "Validation failed");
        response.putAll(fieldErrors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleParseError(HttpMessageNotReadableException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("error", "Invalid request body");
        response.put("message", "Wrong format of data");
        return ResponseEntity.badRequest().body(response);
    }
}
