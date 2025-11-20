package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleTrackingRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleTrackingService {

    private final VehicleTrackingRepository repo;

    public VehicleTrackingService(VehicleTrackingRepository repo) {
        this.repo = repo;
    }

    public List<VehicleTracking> findAll() {
        return repo.findAll();
    }

    /**
     * Find by String id (UUID stored as String)
     */
    public Optional<VehicleTracking> findById(String id) {
        return repo.findById(id);
    }

    /**
     * Save VehicleTracking, auto-generate ID if null
     */
    public VehicleTracking save(VehicleTracking v) {
        if (v.getId() == null) {
            v.setId(UUID.randomUUID().toString());
        }
        return repo.save(v);
    }

    /**
     * Find latest tracking for a driver
     */
    public Optional<VehicleTracking> findLatestByDriver(String driverId) {
        return repo.findTopByDriverIdOrderByTimestampDesc(driverId);
    }

    /**
     * Find latest tracking for a vehicle
     */
    public Optional<VehicleTracking> findLatestByVehicle(String vehicleNumber) {
        return repo.findTopByVehicleNumberOrderByTimestampDesc(vehicleNumber);
    }

    /**
     * Delete by String ID
     */
    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("VehicleTracking not found with id: " + id);
        }
        repo.deleteById(id);
    }
}
