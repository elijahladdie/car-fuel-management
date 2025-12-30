package com.carmanagement.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CLI Model for Car entity
 * Simplified POJO matching API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private List<FuelEntry> fuelEntries;
}
