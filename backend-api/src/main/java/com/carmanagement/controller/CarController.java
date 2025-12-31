package com.carmanagement.controller;

import com.carmanagement.dto.CarRequest;
import com.carmanagement.dto.FuelEntryRequest;
import com.carmanagement.dto.FuelStats;
import com.carmanagement.model.Car;
import com.carmanagement.model.FuelEntry;
import com.carmanagement.service.CarService;
import com.carmanagement.service.FuelService;
import com.carmanagement.util.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for Car Management
 * Exposes endpoints at /api/cars
 */
@RestController
@RequestMapping("/api/cars")
@Tag(name = "Car Management", description = "Operations for managing cars and fuel entries")
public class CarController {
    
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);
    
    private final CarService carService;
    private final FuelService fuelService;
    
    @Autowired
    public CarController(CarService carService, FuelService fuelService) {
        this.carService = carService;
        this.fuelService = fuelService;
    }
    
    /**
     * POST /api/cars
     * Create a new car
     */
    @Operation(summary = "Create a new car", description = "Register a new vehicle in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Car created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Car>> createCar(@Valid @RequestBody CarRequest request) {
        logger.info("POST /api/cars - Creating car: {} {} ({})", 
            request.getBrand(), request.getModel(), request.getYear());
        
        Car createdCar = carService.createCar(request);
        
        // Build Location header with resource URI        
        return ResponseHandler.success(
            createdCar, "Car created successfully", HttpStatus.CREATED
        );
    }
    
    /**
     * GET /api/cars
     * Retrieve all cars
     */
    @Operation(summary = "Get all cars", description = "Retrieve a list of all registered vehicles")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<com.carmanagement.dto.ApiResponse<List<Car>>> getAllCars() {
        logger.info("GET /api/cars - Retrieving all cars");
        
        List<Car> cars = carService.getAllCars();
        
        return com.carmanagement.util.ResponseHandler.success(cars);
    }
    
    /**
     * GET /api/cars/{id}
     * Retrieve a specific car by ID
     */
    @Operation(summary = "Get car by ID", description = "Retrieve details of a specific car")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the car"),
        @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.carmanagement.dto.ApiResponse<Car>> getCarById(
            @Parameter(description = "ID of the car to be retrieved") @PathVariable Long id) {
        logger.info("GET /api/cars/{} - Retrieving car", id);
        
        Car car = carService.getCarById(id);
        
        return com.carmanagement.util.ResponseHandler.success(car);
    }
    
    /**
     * POST /api/cars/{id}/fuel
     * Add a fuel entry to a car
     */
    @Operation(summary = "Add fuel entry", description = "Record a new fueling event for a car")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fuel entry created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or odometer reading"),
        @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @PostMapping("/{id}/fuel")
    public ResponseEntity<com.carmanagement.dto.ApiResponse<FuelEntry>> addFuelEntry(
            @Parameter(description = "ID of the car") @PathVariable Long id,
            @Valid @RequestBody FuelEntryRequest request) {
        logger.info("POST /api/cars/{}/fuel - Adding fuel entry: {}L at {} (odometer: {}km)", 
            id, request.getLiters(), request.getPrice(), request.getOdometer());
        
        FuelEntry createdEntry = fuelService.addFuelEntry(id, request);
        
        return com.carmanagement.util.ResponseHandler.success(
            createdEntry, "Fuel entry added successfully", HttpStatus.CREATED
        );
    }
    
    /**
     * GET /api/cars/{id}/fuel/stats
     * Get fuel statistics for a car
     */
    @Operation(summary = "Get fuel statistics", description = "Calculate fuel consumption statistics for a car")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics"),
        @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<com.carmanagement.dto.ApiResponse<FuelStats>> getFuelStatistics(
            @Parameter(description = "ID of the car") @PathVariable Long id) {
        logger.info("GET /api/cars/{}/fuel/stats - Retrieving fuel statistics", id);
        
        FuelStats stats = fuelService.calculateStatistics(id);
        
        return com.carmanagement.util.ResponseHandler.success(stats);
    }
}
