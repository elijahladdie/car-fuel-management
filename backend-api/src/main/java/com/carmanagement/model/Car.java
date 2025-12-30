package com.carmanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Car Entity
 * Represents a vehicle in the car management system
 */
@Data
@NoArgsConstructor
public class Car {
    
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    
    @JsonManagedReference
    private List<FuelEntry> fuelEntries = new ArrayList<>();
    
    /**
     * Constructor for creating a new car
     * @param brand Vehicle manufacturer
     * @param model Vehicle model name
     * @param year Manufacturing year
     */
    public Car(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelEntries = new ArrayList<>();
    }
    
    /**
     * Add a fuel entry to this car
     * @param fuelEntry Fuel entry to add
     */
    public void addFuelEntry(FuelEntry fuelEntry) {
        if (this.fuelEntries == null) {
            this.fuelEntries = new ArrayList<>();
        }
        this.fuelEntries.add(fuelEntry);
    }
}
