package com.carmanagement.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CLI Model for FuelStats
 * Matches API FuelStats DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelStats {
    private Double totalFuel;
    private Double totalCost;
    private Double averageConsumption;
}
