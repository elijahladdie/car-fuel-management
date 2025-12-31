package com.carmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CarManagementApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(CarManagementApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Car Management Application...");

        ConfigurableApplicationContext context =
                SpringApplication.run(CarManagementApplication.class, args);

        Environment env = context.getEnvironment();

        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        String baseUrl = "http://localhost:" + port + contextPath;

        logger.info("Car Management Application started successfully!");
        logger.info("Available endpoints:");
        logger.info("  REST API: {}{}", baseUrl, "/api/cars");
        logger.info("  Servlet: {}{}", baseUrl, "/servlet/fuel-stats?carId=<id>");
        logger.info("  Swagger UI: {}{}", baseUrl, "/swagger-ui/index.html");
    }

    @GetMapping("favicon.ico")
    public void returnNoFavicon() {
        // Prevents favicon 404 logs
    }
}
