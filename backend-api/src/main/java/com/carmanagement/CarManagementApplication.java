package com.carmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main Spring Boot Application Entry Point
 * Car Management and Fuel Tracking System
 */
@SpringBootApplication
@RestController
public class CarManagementApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(CarManagementApplication.class);
    
    public static void main(String[] args) {
        logger.info("Starting Car Management Application...");
        
        ConfigurableApplicationContext context = SpringApplication.run(
            CarManagementApplication.class, args
        );
        
        logger.info("Car Management Application started successfully!");
        logger.info("Available endpoints:");
        logger.info("  REST API: http://localhost:8080/api/cars");
        logger.info("  Servlet: http://localhost:8080/servlet/fuel-stats?carId=<id>");
        logger.info("  Swagger UI: http://localhost:8080/swagger-ui/index.html");
    }

    @GetMapping("favicon.ico")
    public void returnNoFavicon() {
        // Returns empty response to prevent 404 errors in logs
    }
}
