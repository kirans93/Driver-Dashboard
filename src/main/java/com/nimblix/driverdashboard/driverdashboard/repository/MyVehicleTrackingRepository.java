package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyVehicleTrackingRepository extends JpaRepository<VehicleTracking, String> {

    // Fetch latest tracking for a vehicle
    Optional<VehicleTracking> findTopByVehicleIdOrderByTimestampDesc(String vehicleId);

    // Fetch all tracking for a vehicle
    List<VehicleTracking> findByVehicleIdOrderByTimestampDesc(String vehicleId);

    // Fetch latest tracking for a driver
    Optional<VehicleTracking> findTopByDriverIdOrderByTimestampDesc(String driverId);
}
