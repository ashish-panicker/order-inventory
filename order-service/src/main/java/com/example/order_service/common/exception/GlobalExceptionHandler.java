package com.example.order_service.common.exception;

import com.example.order_service.common.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
/**
 * GlobalExceptionHandler class.
 * Custom exception class for GlobalHandler errors.
 */
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    return fieldName + ": " + error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));

        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaughtException(
            Exception ex, HttpServletRequest request) {
        ApiErrorResponse response = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
