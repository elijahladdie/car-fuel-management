package com.carmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Fuel Statistics response
 * Contains aggregated fuel consumption data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelStats {
    
    /**
     * Sum of all fuel entries in liters
     */
    private Double totalFuel;
    
    /**
     * Cumulative expenditure on fuel
     */
    private Double totalCost;
    
    /**
     * Average fuel consumption in liters per 100km
     * Null if insufficient data (less than 2 entries)
     */
    private Double averageConsumption;
}
