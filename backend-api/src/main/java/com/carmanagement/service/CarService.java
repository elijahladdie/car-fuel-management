package com.carmanagement.service;

import com.carmanagement.dto.CarRequest;
import com.carmanagement.exception.CarNotFoundException;
import com.carmanagement.model.Car;
import com.carmanagement.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Car business logic
 * Orchestrates car-related operations
 */
@Service
public class CarService {
    
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    
    private final CarRepository carRepository;
    
    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    
    /**
     * Create a new car from request DTO
     * Validates input, converts to entity, and persists
     * 
     * @param request CarRequest DTO containing car details
     * @return Saved Car entity
     */
    public Car createCar(CarRequest request) {
        logger.info("Creating new car: {} {} ({})", 
            request.getBrand(), request.getModel(), request.getYear());
        
        // Validate input parameters (additional business validation if needed)
        validateCarRequest(request);
        
        // Check if car with same brand, model, and year already exists
        String brand = request.getBrand().trim();
        String model = request.getModel().trim();
        int year = request.getYear();
        
        if (carRepository.existsByBrandAndModelAndYear(brand, model, year)) {
            logger.warn("Car already exists: {} {} ({})", brand, model, year);
            throw new IllegalArgumentException(
                String.format("Car already exists: %s %s (%d)", brand, model, year)
            );
        }
        
        // Convert DTO to entity
        Car car = new Car(brand, model, year);
        
        // Persist via repository
        Car savedCar = carRepository.save(car);
        
        logger.info("Successfully created car with ID: {}", savedCar.getId());
        return savedCar;
    }
    
    /**
     * Retrieve all cars in the system
     * 
     * @return List of all registered vehicles
     */
    public List<Car> getAllCars() {
        logger.debug("Retrieving all cars");
        List<Car> cars = carRepository.findAll();
        logger.info("Retrieved {} cars", cars.size());
        return cars;
    }
    
    /**
     * Get a car by its unique identifier
     * 
     * @param id Car ID
     * @return Car entity
     * @throws CarNotFoundException if car does not exist
     */
    public Car getCarById(Long id) {
        logger.debug("Fetching car with ID: {}", id);
        return carRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Car not found with ID: {}", id);
                return new CarNotFoundException(id);
            });
    }
    
    /**
     * Additional validation for car creation
     * 
     * @param request CarRequest to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCarRequest(CarRequest request) {
        if (request.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        if (request.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
        
        // Validate if provided year is not in future
        int currentYear = java.time.Year.now().getValue();
        if (request.getYear() > currentYear + 1) {
            throw new IllegalArgumentException(
                "Year cannot be more than one year in the future: " + request.getYear()
            );
        }
    }
}
