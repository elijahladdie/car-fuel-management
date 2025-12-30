package com.carmanagement.servlet;

import com.carmanagement.dto.ApiResponse;
import com.carmanagement.dto.FuelStats;
import com.carmanagement.exception.CarNotFoundException;
import com.carmanagement.service.FuelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Servlet for retrieving fuel statistics
 * Demonstrates servlet lifecycle management alongside Spring Boot
 * Accessible at: /servlet/fuel-stats?carId=<id>
 */
@Component
public class FuelStatsServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(FuelStatsServlet.class);
    private static final long serialVersionUID = 1L;
    
    private final FuelService fuelService;
    private final ObjectMapper objectMapper;
    
    /**
     * Constructor with dependency injection
     * @param fuelService Injected FuelService
     */
    @Autowired
    public FuelStatsServlet(FuelService fuelService) {
        this.fuelService = fuelService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    /**
     * Handle GET requests for fuel statistics
     * 
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if writing to response fails
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Servlet GET request received: {}", req.getRequestURI());
        
        // Set response content type and encoding
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            // Manually extract carId parameter
            String carIdParam = req.getParameter("carId");
            
            // Validate parameter presence
            if (carIdParam == null || carIdParam.trim().isEmpty()) {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    "Missing required parameter: carId", "MISSING_PARAMETER");
                return;
            }
            
            // Parse to Long, handling NumberFormatException
            Long carId;
            try {
                carId = Long.parseLong(carIdParam.trim());
            } catch (NumberFormatException e) {
                logger.error("Invalid carId format: {}", carIdParam);
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                    "Invalid carId format. Must be a valid number.", "INVALID_FORMAT");
                return;
            }
            
            // Invoke service to get statistics
            FuelStats stats = fuelService.calculateStatistics(carId);
            
            // Wrap in ApiResponse and serialize
            ApiResponse<FuelStats> response = ApiResponse.success(stats);
            String jsonResponse = objectMapper.writeValueAsString(response);
            
            // Write JSON to response output stream
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = resp.getWriter();
            writer.write(jsonResponse);
            writer.flush();
            
            logger.info("Successfully returned fuel statistics for car ID: {}", carId);
            
        } catch (CarNotFoundException e) {
            logger.error("Car not found in servlet: {}", e.getMessage());
            sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, 
                e.getMessage(), "CAR_NOT_FOUND");
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument in servlet: {}", e.getMessage());
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
                e.getMessage(), "INVALID_ARGUMENT");
            
        } catch (Exception e) {
            logger.error("Unexpected error in servlet", e);
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred", "INTERNAL_ERROR");
        }
    }
    
    /**
     * Send a JSON error response
     * 
     * @param resp HttpServletResponse
     * @param statusCode HTTP status code
     * @param message Error message
     * @param errorCode Custom error code
     * @throws IOException if writing fails
     */
    private void sendErrorResponse(HttpServletResponse resp, int statusCode, 
                                   String message, String errorCode) throws IOException {
        resp.setStatus(statusCode);
        
        ApiResponse<Object> response = ApiResponse.error(
            message,
            101, // Default error resp_code
            errorCode, // Using generic errors field for specific code
            null
        );
        
        String jsonError = objectMapper.writeValueAsString(response);
        
        PrintWriter writer = resp.getWriter();
        writer.write(jsonError);
        writer.flush();
    }
}
