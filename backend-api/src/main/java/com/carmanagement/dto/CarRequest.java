package com.carmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Car creation requests
 * Contains validation constraints for input data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    
    @NotBlank(message = "Brand is required and cannot be blank")
    private String brand;
    
    @NotBlank(message = "Model is required and cannot be blank")
    private String model;
    
    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be at least 1900")
    @Max(value = 2100, message = "Year cannot exceed 2100")
    private Integer year;
}
