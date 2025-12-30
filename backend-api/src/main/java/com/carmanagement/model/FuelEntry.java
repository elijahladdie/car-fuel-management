package com.carmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FuelEntry Entity
 * Represents a fuel entry record for a car
 */
@Data
@NoArgsConstructor
public class FuelEntry {
    
    private Long id;
    private Double liters;
    private Double price;
    private Integer odometer;
    private LocalDateTime timestamp;
    
    @JsonBackReference
    private Car car;
    
    /**
     * Constructor for creating a new fuel entry
     * @param liters Fuel quantity added
     * @param price Total cost of fuel
     * @param odometer Vehicle mileage reading
     * @throws IllegalArgumentException if any value is not positive
     */
    public FuelEntry(Double liters, Double price, Integer odometer) {
        validatePositive(liters, "liters");
        validatePositive(price, "price");
        validatePositive(odometer, "odometer");
        
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Validates that a value is positive
     * @param value Value to validate
     * @param fieldName Name of the field being validated
     * @throws IllegalArgumentException if value is null or not positive
     */
    private void validatePositive(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new IllegalArgumentException(
                fieldName + " must be a positive value, got: " + value
            );
        }
    }
    
    /**
     * Set the car for this fuel entry
     * @param car Car entity
     */
    public void setCar(Car car) {
        this.car = car;
    }
}
