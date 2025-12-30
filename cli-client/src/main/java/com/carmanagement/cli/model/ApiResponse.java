package com.carmanagement.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CLI Wrapper for API Responses
 * Matches the backend ApiResponse structure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    
    private boolean success;
    
    @JsonProperty("resp_msg")
    private String respMsg;
    
    @JsonProperty("resp_code")
    private int respCode;
    
    private T data;
    
    private Object errors;
}
