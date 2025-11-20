package com.nimblix.driverdashboard.driverdashboard.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;

@Repository
public interface MyVehicleBusRouteRepository extends JpaRepository<BusRoute, String> {

	// Correct query by vehicle's UUID

	Optional<BusRoute> findTopByVehicle_IdAndDate(String vehicleId, LocalDate date);



}
