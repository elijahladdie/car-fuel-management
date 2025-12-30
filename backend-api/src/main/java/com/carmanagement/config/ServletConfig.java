package com.carmanagement.config;

import com.carmanagement.servlet.FuelStatsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for registering custom servlets
 * Programmatically registers FuelStatsServlet with Spring Boot
 */
@Configuration
public class ServletConfig {
    
    /**
     * Register FuelStatsServlet programmatically
     * Maps servlet to /servlet/fuel-stats URL pattern
     * 
     * @param fuelStatsServlet Autowired servlet instance
     * @return ServletRegistrationBean for the fuel stats servlet
     */
    @Bean
    public ServletRegistrationBean<FuelStatsServlet> fuelStatsServletRegistration(
            FuelStatsServlet fuelStatsServlet) {
        
        ServletRegistrationBean<FuelStatsServlet> registration = 
            new ServletRegistrationBean<>(fuelStatsServlet, "/servlet/fuel-stats");
        
        registration.setName("FuelStatsServlet");
        registration.setLoadOnStartup(1);
        
        return registration;
    }
}
