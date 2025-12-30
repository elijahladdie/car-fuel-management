package com.carmanagement.repository;

import com.carmanagement.model.FuelEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory repository for FuelEntry entities
 * Maintains fuel entries with automatic ID generation
 */
@Repository
public class FuelEntryRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(FuelEntryRepository.class);
    
    private final ConcurrentHashMap<Long, FuelEntry> fuelEntryStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);
    
    /**
     * Store a fuel entry record
     * @param entry FuelEntry to save
     * @return Saved fuel entry with assigned ID
     */
    public FuelEntry save(FuelEntry entry) {
        if (entry.getId() == null) {
            entry.setId(idGenerator.getAndIncrement());
        }
        fuelEntryStorage.put(entry.getId(), entry);
        logger.info("Saved fuel entry with ID: {} - {}L at {} (odometer: {}km)", 
            entry.getId(), entry.getLiters(), entry.getPrice(), entry.getOdometer());
        return entry;
    }
    
    /**
     * Retrieve car-specific fuel entries sorted by odometer reading
     * @param carId ID of the car
     * @return List of fuel entries for the specified car, sorted by odometer
     */
    public List<FuelEntry> findByCarId(Long carId) {
        List<FuelEntry> entries = fuelEntryStorage.values().stream()
            .filter(entry -> entry.getCar() != null && 
                           carId.equals(entry.getCar().getId()))
            .sorted(Comparator.comparing(FuelEntry::getOdometer))
            .collect(Collectors.toList());
        
        logger.debug("Retrieved {} fuel entries for car ID: {}", entries.size(), carId);
        return entries;
    }
    
    /**
     * Get all fuel entries in the system
     * @return List of all fuel entries
     */
    public List<FuelEntry> findAll() {
        return fuelEntryStorage.values().stream()
            .sorted(Comparator.comparing(FuelEntry::getTimestamp))
            .collect(Collectors.toList());
    }
    
    /**
     * Get the total number of fuel entries
     * @return Count of fuel entries
     */
    public long count() {
        return fuelEntryStorage.size();
    }
    
    /**
     * Clear all fuel entries (useful for testing)
     */
    public void clear() {
        fuelEntryStorage.clear();
        logger.info("Cleared all fuel entries from repository");
    }
}
