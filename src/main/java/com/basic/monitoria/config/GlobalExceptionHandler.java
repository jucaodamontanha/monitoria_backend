package com.basic.monitoria.config;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {


    // Bean Validation (DTOs @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Validation error");

        Map<String, String> fields = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), Optional.ofNullable(fe.getDefaultMessage()).orElse("Invalid value"));
        }
        body.put("fields", fields);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");

        String message = "Malformed JSON or invalid value";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();
            Object value = ife.getValue();

            if (targetType.isEnum()) {
                Object[] constants = targetType.getEnumConstants();
                List<String> acceptedNames = new ArrayList<>();
                List<String> acceptedLabels = new ArrayList<>();
                for (Object c : constants) {
                    Enum<?> e = (Enum<?>) c;
                    acceptedNames.add(e.name());
                    try {
                        // tenta chamar getLabel() via reflexão (se existir)
                        var m = targetType.getMethod("getLabel");
                        Object lbl = m.invoke(c);
                        if (lbl != null) acceptedLabels.add(lbl.toString());
                    } catch (Exception ignored) {}
                }

                if (!acceptedLabels.isEmpty()) {
                    message = String.format(
                            "Invalid value '%s' for %s. Accepted names: %s. Accepted labels: %s",
                            value, targetType.getSimpleName(),
                            String.join(", ", acceptedNames),
                            String.join(", ", acceptedLabels)
                    );
                } else {
                    message = String.format(
                            "Invalid value '%s' for %s. Accepted: %s",
                            value, targetType.getSimpleName(),
                            String.join(", ", acceptedNames)
                    );
                }
            }
        }
        body.put("message", message);
        return ResponseEntity.badRequest().body(body);
    }


    // Negócio / validações manuais (email/login repetidos, not found etc.)

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

}
