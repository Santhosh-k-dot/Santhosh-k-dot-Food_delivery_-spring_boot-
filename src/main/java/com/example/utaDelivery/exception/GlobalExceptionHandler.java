package com.example.utaDelivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false), "Resource not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ValidationError> validationErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ValidationError(((FieldError) error).getObjectName(), ((FieldError) error).getField(), ((FieldError) error).getRejectedValue(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Validation error", request.getDescription(false), validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Server Error: " + ex.getMessage(), request.getDescription(false), "Unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private String message;
        private String details;
        private Object subErrors;

        public ErrorResponse(LocalDateTime timestamp, String message, String details, Object subErrors) {
            this.timestamp = timestamp;
            this.message = message;
            this.details = details;
            this.subErrors = subErrors;
        }

        // Getters and Setters
    }

    public static class ValidationError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        public ValidationError(String object, String field, Object rejectedValue, String message) {
            this.object = object;
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        // Getters and Setters
    }
}
