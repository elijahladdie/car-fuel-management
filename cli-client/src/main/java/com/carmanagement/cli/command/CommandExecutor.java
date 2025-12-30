package com.carmanagement.cli.command;

import com.carmanagement.cli.client.ApiClient;
import com.carmanagement.cli.model.Car;
import com.carmanagement.cli.model.FuelEntry;
import com.carmanagement.cli.model.FuelStats;
import com.carmanagement.cli.parser.CommandParser.Command;

import java.util.Map;

/**
 * Command Executor for CLI operations
 * Orchestrates command execution and API calls
 */
public class CommandExecutor {
    
    private final ApiClient apiClient;
    
    /**
     * Constructor with dependency injection
     * 
     * @param apiClient API client instance
     */
    public CommandExecutor(ApiClient apiClient) {
        this.apiClient = apiClient;
    }
    
    /**
     * Execute a parsed command
     * 
     * @param command Command to execute
     * @throws Exception if execution fails
     */
    public void execute(Command command) throws Exception {
        switch (command.getName()) {
            case "create-car":
                executeCreateCar(command.getParameters());
                break;
            
            case "add-fuel":
                executeAddFuel(command.getParameters());
                break;
            
            case "fuel-stats":
                executeFuelStats(command.getParameters());
                break;
            
            case "help":
                executeHelp();
                break;
            
            default:
                throw new IllegalArgumentException("Unknown command: " + command.getName());
        }
    }
    
    /**
     * Execute create-car command
     * 
     * @param params Command parameters
     * @throws Exception if execution fails
     */
    private void executeCreateCar(Map<String, String> params) throws Exception {
        String brand = params.get("brand");
        String model = params.get("model");
        
        // Validate and parse year
        int year;
        try {
            year = Integer.parseInt(params.get("year"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid year format: '" + params.get("year") + "'. Must be a valid number."
            );
        }
        
        // Call API
        System.out.println("Creating car: " + brand + " " + model + " (" + year + ")...");
        Car car = apiClient.createCar(brand, model, year);
        if (car != null) {
            System.out.println("DEBUG: Car properties - Brand: " + car.getBrand() + ", Model: " + car.getModel() + ", ID: " + car.getId());
        } else {
            System.out.println("DEBUG: Car object is NULL");
            return;
        }

        // Format success message
        System.out.println("\n✓ Car created successfully!");
        System.out.println("  Brand:  " + car.getBrand());
        System.out.println("  Model:  " + car.getModel());
        System.out.println("  Year:   " + car.getYear());
        System.out.println("  ID:     " + car.getId());
    }
    
    /**
     * Execute add-fuel command
     * 
     * @param params Command parameters
     * @throws Exception if execution fails
     */
    private void executeAddFuel(Map<String, String> params) throws Exception {
        // Parse and validate parameters
        Long carId;
        double liters;
        double price;
        int odometer;
        
        try {
            carId = Long.parseLong(params.get("carId"));
            liters = Double.parseDouble(params.get("liters"));
            price = Double.parseDouble(params.get("price"));
            odometer = Integer.parseInt(params.get("odometer"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid numeric format in parameters. Please check your input."
            );
        }
        
        // Validate positive values
        if (liters <= 0 || price <= 0 || odometer <= 0) {
            throw new IllegalArgumentException(
                "All values (liters, price, odometer) must be positive."
            );
        }
        
        // Call API
        System.out.println("Adding fuel entry to car ID " + carId + "...");
        FuelEntry entry = apiClient.addFuelEntry(carId, liters, price, odometer);
        System.out.println(entry);
        
        // Display confirmation
        System.out.println("\n✓ Fuel entry added successfully!");
        System.out.println("  Entry ID:  " + entry.getId());
        System.out.println("  Liters:    " + String.format("%.2f", entry.getLiters()) + " L");
        System.out.println("  Price:     " + String.format("%.2f", entry.getPrice()));
        System.out.println("  Odometer:  " + entry.getOdometer() + " km");
        System.out.println("  Timestamp: " + entry.getTimestamp());
    }
    
    /**
     * Execute fuel-stats command
     * 
     * @param params Command parameters
     * @throws Exception if execution fails
     */
    private void executeFuelStats(Map<String, String> params) throws Exception {
        // Parse carId
        Long carId;
        try {
            carId = Long.parseLong(params.get("carId"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid carId format: '" + params.get("carId") + 
                "'. Must be a valid number."
            );
        }
        
        // Retrieve statistics
        System.out.println("Retrieving fuel statistics for car ID " + carId + "...");
        FuelStats stats = apiClient.getFuelStatistics(carId);
        
        // Format output
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  Fuel Statistics for Car ID " + carId);
        System.out.println("=".repeat(50));
        System.out.println("  Total Fuel:          " + 
            String.format("%.2f", stats.getTotalFuel()) + " L");
        System.out.println("  Total Cost:          " + 
            String.format("%.2f", stats.getTotalCost()));
        
        // Handle null average consumption
        if (stats.getAverageConsumption() != null) {
            System.out.println("  Average Consumption: " + 
                String.format("%.2f", stats.getAverageConsumption()) + " L/100km");
        } else {
            System.out.println("  Average Consumption: N/A (insufficient data)");
        }
        System.out.println("=".repeat(50));
    }
    
    /**
     * Display help information
     */
    private void executeHelp() {
        System.out.println("\nCar Management CLI - Help");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("Available Commands:");
        System.out.println();
        System.out.println("1. create-car");
        System.out.println("   Create a new car in the system");
        System.out.println("   Usage: create-car --brand <brand> --model <model> --year <year>");
        System.out.println("   Example: create-car --brand Toyota --model Corolla --year 2018");
        System.out.println();
        System.out.println("2. add-fuel");
        System.out.println("   Add a fuel entry to a car");
        System.out.println("   Usage: add-fuel --carId <id> --liters <amount> --price <cost> --odometer <reading>");
        System.out.println("   Example: add-fuel --carId 1 --liters 45.5 --price 65.50 --odometer 10500");
        System.out.println();
        System.out.println("3. fuel-stats");
        System.out.println("   Get fuel statistics for a car");
        System.out.println("   Usage: fuel-stats --carId <id>");
        System.out.println("   Example: fuel-stats --carId 1");
        System.out.println();
        System.out.println("4. help");
        System.out.println("   Display this help message");
        System.out.println("   Usage: help");
        System.out.println();
        System.out.println("=".repeat(60));
    }
}
