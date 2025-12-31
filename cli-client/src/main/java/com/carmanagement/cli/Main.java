package com.carmanagement.cli;

import com.carmanagement.cli.client.ApiClient;
import com.carmanagement.cli.command.CommandExecutor;
import com.carmanagement.cli.parser.CommandParser;

/**
 * Main CLI Application Entry Point
 * Car Management System Command Line Interface
 */
public class Main {
    
    /**
     * Main entry point for CLI application
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            // Validate argument presence
            if (args.length == 0) {
                printUsageAndExit();
            }
            
            // Initialize components
            ApiClient apiClient = new ApiClient();
            CommandParser parser = new CommandParser();
            CommandExecutor executor = new CommandExecutor(apiClient);
            
            // Parse command from arguments
            CommandParser.Command command = parser.parseCommand(args);
            
            // Execute command
            executor.execute(command);
            
            // Exit with success
            System.exit(0);
            
        } catch (CommandParser.InvalidCommandException e) {
            // Handle command parsing errors
            System.err.println("ERROR: " + e.getMessage());
            System.err.println();
            printUsage();
            System.exit(1);
            
        } catch (ApiClient.ApiException e) {
            // Handle API errors
            System.err.println("API ERROR: " + e.getMessage());
            if (e.getStatusCode() == 404) {
                System.err.println("The requested resource was not found.");
            } else if (e.getStatusCode() == 400) {
                System.err.println("Invalid request. Please check your input parameters.");
            }
            System.exit(1);
            
        } catch (java.io.IOException e) {
            // Handle connection errors
            System.err.println("CONNECTION ERROR: " + e.getMessage());
            System.err.println("Please ensure the API server is running and accessible.");
            System.exit(1);
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            System.err.println("VALIDATION ERROR: " + e.getMessage());
            System.exit(1);
            
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("UNEXPECTED ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Print usage information and exit
     */
    private static void printUsageAndExit() {
        printUsage();
        System.exit(0);
    }
    
    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Car Management CLI");
        System.out.println("==================");
        System.out.println();
        System.out.println("Usage: java -jar car-cli.jar <command> <arguments>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  create-car  --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel    --carId <id> --liters <amount> --price <cost> --odometer <reading>");
        System.out.println("  fuel-stats  --carId <id>");
        System.out.println("  help");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar car-cli.jar create-car --brand Toyota --model Corolla --year 2018");
        System.out.println("  java -jar car-cli.jar add-fuel --carId 1 --liters 45.5 --price 65.50 --odometer 10500");
        System.out.println("  java -jar car-cli.jar fuel-stats --carId 1");
        System.out.println();
        System.out.println("For detailed help, run: java -jar car-cli.jar help");
    }
}
