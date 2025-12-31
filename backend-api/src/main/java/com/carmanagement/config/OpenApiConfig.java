package com.carmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI / Swagger Documentation
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI carManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Car Management & Fuel Tracking API")
                .description("REST API for managing cars and tracking fuel efficiency. " +
                           "Includes endpoints for car creation, fuel entry recording, and statistical analysis.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Car Management Team")
                    .email("support@carmanagement.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://carmanagement.com/licenses")));
    }
}
