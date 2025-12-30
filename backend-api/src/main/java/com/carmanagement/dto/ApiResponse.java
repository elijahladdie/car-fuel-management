package com.carmanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API Response Wrapper
 * Matches the requested JSON format:
 * {
 *   "success": true,
 *   "resp_msg": "Success",
 *   "resp_code": 100,
 *   "data": { ... },
 *   "errors": null
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private boolean success;
    
    @JsonProperty("resp_msg")
    private String respMsg;
    
    @JsonProperty("resp_code")
    private int respCode;
    
    private T data;
    
    private Object pagination;
    
    private Object errors;
    
    // Static factory methods for convenience
    
    public static <T> ApiResponse<T> success(T data, String message, int respCode) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .respMsg(message)
                .respCode(respCode)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Success", 100);
    }

    public static <T> ApiResponse<T> error(String message, int respCode, Object errors, T emptyData) {
        return ApiResponse.<T>builder()
                .success(false)
                .respMsg(message)
                .respCode(respCode)
                .errors(errors)
                .data(emptyData)
                .build();
    }
}
