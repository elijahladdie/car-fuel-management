package com.carmanagement.cli.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Parser for CLI arguments
 * Parses command-line arguments into structured command objects
 */
public class CommandParser {
    
    /**
     * Parse command-line arguments
     * 
     * @param args Command-line arguments
     * @return Parsed Command object
     * @throws InvalidCommandException if command is invalid
     */
    public Command parseCommand(String[] args) throws InvalidCommandException {
        // Validate minimum argument count
        if (args == null || args.length == 0) {
            throw new InvalidCommandException("No command specified. Use: <command> <args>");
        }
        
        // Extract command name
        String commandName = args[0].toLowerCase();
        
        // Parse remaining arguments as key-value pairs
        Map<String, String> parameters = parseParameters(Arrays.copyOfRange(args, 1, args.length));
        
        // Create and validate command
        Command command = new Command(commandName, parameters);
        validateCommand(command);
        
        return command;
    }
    
    /**
     * Parse key-value pairs from arguments (--key value format)
     * 
     * @param args Argument array
     * @return Map of parameters
     */
    private Map<String, String> parseParameters(String[] args) {
        Map<String, String> parameters = new HashMap<>();
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2); // Remove --
                
                // Check if value exists
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    String value = args[i + 1];
                    parameters.put(key, value);
                    i++; // Skip next argument as it's the value
                } else {
                    parameters.put(key, ""); // Flag without value
                }
            }
        }
        
        return parameters;
    }
    
    /**
     * Validate command has required parameters
     * 
     * @param command Command to validate
     * @throws InvalidCommandException if validation fails
     */
    private void validateCommand(Command command) throws InvalidCommandException {
        switch (command.getName()) {
            case "create-car":
                requireParameters(command, "brand", "model", "year");
                break;
            
            case "add-fuel":
                requireParameters(command, "carId", "liters", "price", "odometer");
                break;
            
            case "fuel-stats":
                requireParameters(command, "carId");
                break;
            
            case "help":
                // No parameters required
                break;
            
            default:
                throw new InvalidCommandException(
                    "Unknown command: " + command.getName() + 
                    ". Available commands: create-car, add-fuel, fuel-stats, help"
                );
        }
    }
    
    /**
     * Check that all required parameters are present
     * 
     * @param command Command to check
     * @param requiredParams Required parameter names
     * @throws InvalidCommandException if any parameter is missing
     */
    private void requireParameters(Command command, String... requiredParams) 
            throws InvalidCommandException {
        List<String> missing = Arrays.stream(requiredParams)
            .filter(param -> !command.getParameters().containsKey(param) || 
                           command.getParameters().get(param).isEmpty())
            .toList();
        
        if (!missing.isEmpty()) {
            throw new InvalidCommandException(
                "Missing required parameters for '" + command.getName() + 
                "': " + String.join(", ", missing)
            );
        }
    }
    
    /**
     * Command value object
     */
    public static class Command {
        private final String name;
        private final Map<String, String> parameters;
        
        public Command(String name, Map<String, String> parameters) {
            this.name = name;
            this.parameters = parameters;
        }
        
        public String getName() {
            return name;
        }
        
        public Map<String, String> getParameters() {
            return parameters;
        }
    }
    
    /**
     * Exception for invalid commands
     */
    public static class InvalidCommandException extends Exception {
        public InvalidCommandException(String message) {
            super(message);
        }
    }
}
