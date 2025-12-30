package com.carmanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API
 * Provides centralized error handling and formatted error responses
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle CarNotFoundException
     * Returns 404 with error message
     */
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Object>> handleCarNotFoundException(
            CarNotFoundException ex) {
        logger.error("Car not found: {}", ex.getMessage());
        return com.carmanagement.util.ResponseHandler.error(
            null,
            ex.getMessage(),
            HttpStatus.NOT_FOUND
        );
    }
    
    /**
     * Handle validation errors from @Valid annotations
     * Returns 400 with validation details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        logger.error("Validation error occurred");
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
            logger.error("Validation error - {}: {}", fieldName, errorMessage);
        });
        
        return com.carmanagement.util.ResponseHandler.error(
            fieldErrors,
            "Input validation error",
            HttpStatus.BAD_REQUEST
        );
    }
    
    /**
     * Handle IllegalArgumentException
     * Returns 400 with reason
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        logger.error("Illegal argument: {}", ex.getMessage());
        return com.carmanagement.util.ResponseHandler.error(
            null,
            ex.getMessage(),
            HttpStatus.BAD_REQUEST
        );
    }
    
    /**
     * Handle generic exceptions
     * Returns 500 with sanitized message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Object>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return com.carmanagement.util.ResponseHandler.error(
            null,
            "An unexpected error occurred. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
