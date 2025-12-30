package com.carmanagement.exception;

/**
 * Custom exception thrown when a car is not found in the system
 */
public class CarNotFoundException extends RuntimeException {
    
    /**
     * Constructor with car ID
     * @param carId ID of the car that was not found
     */
    public CarNotFoundException(Long carId) {
        super("Car not found with ID: " + carId);
    }
    
    /**
     * Constructor with custom message
     * @param message Custom error message
     */
    public CarNotFoundException(String message) {
        super(message);
    }
}
