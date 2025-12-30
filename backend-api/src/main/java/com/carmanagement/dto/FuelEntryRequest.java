package com.carmanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Fuel Entry creation requests
 * Contains validation constraints ensuring all values are positive
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelEntryRequest {
    
    @NotNull(message = "Liters is required")
    @Positive(message = "Liters must be a positive value")
    private Double liters;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private Double price;
    
    @NotNull(message = "Odometer reading is required")
    @Positive(message = "Odometer reading must be a positive value")
    private Integer odometer;
}
