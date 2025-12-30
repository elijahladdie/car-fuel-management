package com.carmanagement.cli.client;

import com.carmanagement.cli.model.ApiResponse;
import com.carmanagement.cli.model.Car;
import com.carmanagement.cli.model.FuelEntry;
import com.carmanagement.cli.model.FuelStats;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Client for Car Management API
 * Handles all REST API communications
 */
public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    /**
     * Initialize API client with default HTTP client and JSON mapper
     */
    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Create a new car
     */
    public Car createCar(String brand, String model, int year) throws IOException, ApiException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("brand", brand);
        requestBody.put("model", model);
        requestBody.put("year", year);
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/cars"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        
        HttpResponse<String> response = sendRequest(request);
        
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            ApiResponse<Car> apiResponse = objectMapper.readValue(
                response.body(), 
                new TypeReference<ApiResponse<Car>>() {}
            );
            return apiResponse.getData();
        } else {
            throw handleErrorResponse(response);
        }
    }
    
    /**
     * Add a fuel entry to a car
     */
    public FuelEntry addFuelEntry(Long carId, double liters, double price, int odometer) 
            throws IOException, ApiException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("liters", liters);
        requestBody.put("price", price);
        requestBody.put("odometer", odometer);
        
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        
        HttpResponse<String> response = sendRequest(request);
        
        if (response.statusCode() == 201 || response.statusCode() == 200) {
            ApiResponse<FuelEntry> apiResponse = objectMapper.readValue(
                response.body(), 
                new TypeReference<ApiResponse<FuelEntry>>() {}
            );
            return apiResponse.getData();
        } else {
            throw handleErrorResponse(response);
        }
    }
    
    /**
     * Get fuel statistics for a car
     */
    public FuelStats getFuelStatistics(Long carId) throws IOException, ApiException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel/stats"))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = sendRequest(request);
        
        if (response.statusCode() == 200) {
            ApiResponse<FuelStats> apiResponse = objectMapper.readValue(
                response.body(), 
                new TypeReference<ApiResponse<FuelStats>>() {}
            );
            return apiResponse.getData();
        } else if (response.statusCode() == 404) {
            throw new ApiException("Car not found with ID: " + carId, response.statusCode());
        } else {
            throw handleErrorResponse(response);
        }
    }
    
    /**
     * Send HTTP request with error handling
     */
    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        } catch (IOException e) {
            throw new IOException("Failed to connect to API server at " + BASE_URL + 
                ". Please ensure the server is running.", e);
        }
    }
    
    /**
     * Handle error responses from API
     */
    private ApiException handleErrorResponse(HttpResponse<String> response) {
        try {
            // Try to parse error JSON into ApiResponse
            ApiResponse<Object> apiResponse = objectMapper.readValue(
                response.body(), 
                new TypeReference<ApiResponse<Object>>() {}
            );
            
            String message = apiResponse.getRespMsg();
            if (message == null || message.isEmpty()) {
                message = "Unknown error";
            }
            return new ApiException(message, response.statusCode());
            
        } catch (Exception e) {
            // If parsing fails, use raw response
            return new ApiException(
                "API error (HTTP " + response.statusCode() + "): " + response.body(),
                response.statusCode()
            );
        }
    }
    
    /**
     * Custom exception for API errors
     */
    public static class ApiException extends Exception {
        private final int statusCode;
        
        public ApiException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
    }
}
