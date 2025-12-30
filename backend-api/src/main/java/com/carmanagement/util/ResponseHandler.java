package com.carmanagement.util;

import com.carmanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * ResponseHandler Utility
 * Standardizes the construction of ResponseEntity objects
 */
public class ResponseHandler {

    /**
     * Build a success response
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(
            T data, 
            String message, 
            HttpStatus status) {
        
        // Default resp_code is 100 for success
        ApiResponse<T> response = ApiResponse.success(data, message, 100);
        
        // Handle pagination validity check if T matches a paginated structure (simplified here)
        if (data instanceof Map && ((Map<?, ?>) data).containsKey("pagination")) {
            response.setPagination(((Map<?, ?>) data).get("pagination"));
            response.setData((T) ((Map<?, ?>) data).get("data"));
        }

        return new ResponseEntity<>(response, status);
    }

    /**
     * Build a success response with default 200 OK
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Success", HttpStatus.OK);
    }

    /**
     * Build an error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(
            Object error, 
            String message, 
            HttpStatus status) {
        
        // Default resp_code is 101 for error
        ApiResponse<T> response = ApiResponse.error(
            message != null ? message : "Something went wrong", 
            101, 
            error, 
            null
        );

        return new ResponseEntity<>(response, status);
    }
    
    /**
     * Build an error response with custom response code
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(
            Object error, 
            String message, 
            int respCode,
            HttpStatus status) {
        
        ApiResponse<T> response = ApiResponse.error(
            message, 
            respCode, 
            error, 
            null
        );

        return new ResponseEntity<>(response, status);
    }
}
