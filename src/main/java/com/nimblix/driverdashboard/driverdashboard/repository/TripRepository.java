package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, String> {

   

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driverId = :driverId AND t.tripDate = :date AND t.status = 'COMPLETED'")
    int countCompletedTrips(@Param("driverId") String driverId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driverId = :driverId AND t.tripDate = :date")
    int countTotalTrips(@Param("driverId") String driverId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driverId = :driverId AND t.tripDate = :date AND t.arrivalTime <= t.scheduledTime")
    int countOnTimeTrips(@Param("driverId") String driverId, @Param("date") LocalDate date);
    
    // Find all trips for a driver on a specific date
    List<Trip> findByDriverIdAndTripDate(String driverId, LocalDate tripDate);

    // Count completed trips for today (for Overview Dashboard)
    int countByDriverIdAndTripDateAndStatus(String driverId, LocalDate tripDate, Trip.TripStatus status);

    // Optional: find trips by route number
    List<Trip> findByDriverIdAndTripDateAndRouteNumber(String driverId, LocalDate tripDate, String routeNumber);
    
    int countByDriverIdAndTripDate(String driverId, LocalDate tripDate);
    
    

    
    List<Trip> findByDriverIdAndTripDateBetween(String driverId, LocalDate start, LocalDate end);
}

