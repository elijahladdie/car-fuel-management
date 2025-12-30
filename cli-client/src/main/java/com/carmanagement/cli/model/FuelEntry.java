package com.carmanagement.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CLI Model for FuelEntry entity
 * Simplified POJO matching API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelEntry {
    private Long id;
    private Double liters;
    private Double price;
    private Integer odometer;
    private String timestamp;
}
