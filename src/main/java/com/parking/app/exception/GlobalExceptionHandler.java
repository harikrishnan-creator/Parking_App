package com.parking.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>>
    handleResourceNotFound(
            ResourceNotFoundException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "status",
                HttpStatus.NOT_FOUND.value());

        response.put(
                "error",
                "NOT_FOUND");

        response.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>>
    handleUnauthorized(
            UnauthorizedException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "status",
                HttpStatus.UNAUTHORIZED.value());

        response.put(
                "error",
                "UNAUTHORIZED");

        response.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(
            ValidationException.class)
    public ResponseEntity<Map<String, Object>>
    handleValidation(
            ValidationException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value());

        response.put(
                "error",
                "VALIDATION_ERROR");

        response.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>>
    handleBeanValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value());

        response.put(
                "error",
                "VALIDATION_ERROR");

        response.put(
                "message",
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
    handleGeneralException(
            Exception ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now());

        response.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value());

        response.put(
                "error",
                "INTERNAL_SERVER_ERROR");

        response.put(
                "message",
                ex.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
