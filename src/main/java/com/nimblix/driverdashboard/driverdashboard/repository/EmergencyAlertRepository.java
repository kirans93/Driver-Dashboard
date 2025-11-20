package com.nimblix.driverdashboard.driverdashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.EmergencyAlert;

@Repository
public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, String> {
    
    // Can add custom queries if needed:
    // List<EmergencyAlert> findByStatus(String status);
    // List<EmergencyAlert> findByDriver_Id(String driverId);
    // List<EmergencyAlert> findByVehicle_Id(String vehicleId);
}
