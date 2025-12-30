package com.carmanagement.service;

import com.carmanagement.dto.FuelEntryRequest;
import com.carmanagement.dto.FuelStats;
import com.carmanagement.model.Car;
import com.carmanagement.model.FuelEntry;
import com.carmanagement.repository.FuelEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Service layer for Fuel Entry business logic
 * Manages fuel entries and calculates statistics
 */
@Service
public class FuelService {
    
    private static final Logger logger = LoggerFactory.getLogger(FuelService.class);
    
    private final FuelEntryRepository fuelEntryRepository;
    private final CarService carService;
    
    @Autowired
    public FuelService(FuelEntryRepository fuelEntryRepository, CarService carService) {
        this.fuelEntryRepository = fuelEntryRepository;
        this.carService = carService;
    }
    
    /**
     * Add a fuel entry to a car
     * Validates car existence and odometer reading progression
     * 
     * @param carId ID of the car
     * @param request FuelEntryRequest containing fuel data
     * @return Created FuelEntry entity
     * @throws IllegalArgumentException if odometer reading is invalid
     */
    public FuelEntry addFuelEntry(Long carId, FuelEntryRequest request) {
        logger.info("Adding fuel entry for car ID: {} - {}L at {} (odometer: {}km)", 
            carId, request.getLiters(), request.getPrice(), request.getOdometer());
        
        // Validate car existence via CarService
        Car car = carService.getCarById(carId);
        
        // Verify odometer reading exceeds previous entries
        validateOdometerReading(carId, request.getOdometer());
        
        // Create FuelEntry entity with current timestamp
        FuelEntry fuelEntry = new FuelEntry(
            request.getLiters(),
            request.getPrice(),
            request.getOdometer()
        );
        
        // Associate entry with car
        fuelEntry.setCar(car);
        
        // Persist entry
        FuelEntry savedEntry = fuelEntryRepository.save(fuelEntry);
        
        // Add to car's fuel entries list
        car.addFuelEntry(savedEntry);
        
        logger.info("Successfully added fuel entry with ID: {}", savedEntry.getId());
        return savedEntry;
    }
    
    /**
     * Calculate fuel statistics for a car
     * Computes total fuel, total cost, and average consumption
     * 
     * @param carId ID of the car
     * @return FuelStats DTO containing statistics
     */
    public FuelStats calculateStatistics(Long carId) {
        logger.info("Calculating fuel statistics for car ID: {}", carId);
        
        // Validate car exists
        carService.getCarById(carId);
        
        // Retrieve all fuel entries for specified car
        List<FuelEntry> entries = fuelEntryRepository.findByCarId(carId);
        
        if (entries.isEmpty()) {
            logger.info("No fuel entries found for car ID: {}", carId);
            return new FuelStats(0.0, 0.0, null);
        }
        
        // Calculate total fuel: sum(entry.liters)
        double totalFuel = entries.stream()
            .mapToDouble(FuelEntry::getLiters)
            .sum();
        
        // Calculate total cost: sum(entry.price)
        double totalCost = entries.stream()
            .mapToDouble(FuelEntry::getPrice)
            .sum();
        
        // Calculate average consumption
        Double averageConsumption = null;
        
        if (entries.size() >= 2) {
            // Get min and max odometer readings
            int minOdometer = entries.stream()
                .mapToInt(FuelEntry::getOdometer)
                .min()
                .orElse(0);
            
            int maxOdometer = entries.stream()
                .mapToInt(FuelEntry::getOdometer)
                .max()
                .orElse(0);
            
            int distanceTraveled = maxOdometer - minOdometer;
            
            if (distanceTraveled > 0) {
                // Formula: (totalFuel / (maxOdometer - minOdometer)) * 100
                averageConsumption = (totalFuel / distanceTraveled) * 100;
                logger.info("Average consumption calculated: {} L/100km", 
                    String.format("%.2f", averageConsumption));
            }
        } else {
            logger.info("Insufficient data for average consumption (minimum 2 entries required)");
        }
        
        FuelStats stats = new FuelStats(totalFuel, totalCost, averageConsumption);
        logger.info("Calculated statistics - Total Fuel: {}L, Total Cost: {}, Avg Consumption: {}", 
            totalFuel, totalCost, averageConsumption != null ? averageConsumption + " L/100km" : "N/A");
        
        return stats;
    }
    
    /**
     * Validates that the new odometer reading is greater than all previous readings
     * 
     * @param carId ID of the car
     * @param newOdometer New odometer reading
     * @throws IllegalArgumentException if odometer reading is not valid
     */
    private void validateOdometerReading(Long carId, Integer newOdometer) {
        List<FuelEntry> existingEntries = fuelEntryRepository.findByCarId(carId);
        
        if (!existingEntries.isEmpty()) {
            FuelEntry lastEntry = existingEntries.stream()
                .max(Comparator.comparing(FuelEntry::getOdometer))
                .orElse(null);
            
            if (lastEntry != null && newOdometer <= lastEntry.getOdometer()) {
                String errorMsg = String.format(
                    "Invalid odometer reading: %d. Must be greater than the last reading: %d",
                    newOdometer, lastEntry.getOdometer()
                );
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }
}
