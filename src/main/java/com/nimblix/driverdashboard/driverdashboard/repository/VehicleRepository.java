package com.nimblix.driverdashboard.driverdashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
	
    boolean existsByVehicleNumber(String vehicleNumber);	
}
