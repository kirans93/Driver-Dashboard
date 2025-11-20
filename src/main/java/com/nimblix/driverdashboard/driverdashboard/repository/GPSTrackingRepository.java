package com.nimblix.driverdashboard.driverdashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.GPSTracking;

@Repository
public interface GPSTrackingRepository extends JpaRepository<GPSTracking, String> {

    // Find all GPS records for a specific vehicle
    List<GPSTracking> findByVehicleId(String vehicleId);

    // Find latest GPS record for a vehicle
    GPSTracking findTopByVehicleIdOrderByTimestampDesc(String vehicleId);

    // Find all GPS records for a specific driver
    List<GPSTracking> findByDriverId(String driverId);

}
