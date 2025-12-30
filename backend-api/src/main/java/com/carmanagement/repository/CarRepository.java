package com.carmanagement.repository;

import com.carmanagement.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe in-memory repository for Car entities
 * Uses ConcurrentHashMap for concurrent access and AtomicLong for ID generation
 */
@Repository
public class CarRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(CarRepository.class);
    
    private final ConcurrentHashMap<Long, Car> carStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);
    
    /**
     * Persist a new car with auto-generated ID
     * @param car Car entity to save
     * @return Saved car with assigned ID
     */
    public Car save(Car car) {
        if (car.getId() == null) {
            car.setId(idGenerator.getAndIncrement());
        }
        carStorage.put(car.getId(), car);
        logger.info("Saved car with ID: {} - {} {} ({})", 
            car.getId(), car.getBrand(), car.getModel(), car.getYear());
        return car;
    }
    
    /**
     * Retrieve a car by its unique identifier
     * @param id Car ID
     * @return Optional containing the car if found, empty otherwise
     */
    public Optional<Car> findById(Long id) {
        Car car = carStorage.get(id);
        if (car != null) {
            logger.debug("Found car with ID: {}", id);
        } else {
            logger.debug("Car not found with ID: {}", id);
        }
        return Optional.ofNullable(car);
    }
    
    /**
     * Return all registered vehicles
     * @return List of all cars in the system
     */
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>(carStorage.values());
        logger.debug("Retrieved {} cars from repository", cars.size());
        return cars;
    }
    
    /**
     * Get the total number of cars in the repository
     * @return Count of cars
     */
    public long count() {
        return carStorage.size();
    }
    
    /**
     * Clear all cars from the repository (useful for testing)
     */
    public void clear() {
        carStorage.clear();
        logger.info("Cleared all cars from repository");
    }
}
