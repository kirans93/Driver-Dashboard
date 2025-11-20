package com.nimblix.driverdashboard.driverdashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, String> {
	
	BusRoute findByDriverName(String driverName);
    BusRoute findByRouteNumber(String routeNumber);
    BusRoute findByVehicleNumber(String vehicleNumber);

}
