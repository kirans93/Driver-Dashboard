package com.nimblix.driverdashboard.driverdashboard.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;

@Repository
public interface VehicleTrackingRepository extends JpaRepository<VehicleTracking, String> {

    // Latest tracking record by driverId
    Optional<VehicleTracking> findTopByDriverIdOrderByTimestampDesc(String driverId);

    // Latest tracking record by vehicleNumber
    Optional<VehicleTracking> findTopByVehicleNumberOrderByTimestampDesc(String vehicleNumber);
    

    Optional<VehicleTracking> findTopByVehicleIdOrderByTimestampDesc(String vehicleId);
    
    @Query("SELECT MIN(v.currentKm) FROM VehicleTracking v WHERE v.driverId = :driverId AND DATE(v.timestamp) = :date")
    Integer findMinKmForToday(@Param("driverId") String driverId, @Param("date") LocalDate date);

    @Query("SELECT MAX(v.currentKm) FROM VehicleTracking v WHERE v.driverId = :driverId AND DATE(v.timestamp) = :date")
    Integer findMaxKmForToday(@Param("driverId") String driverId, @Param("date") LocalDate date);
    
    
    
    
    List<VehicleTracking> findByDriverIdAndTimestampBetween(String driverId, LocalDateTime start, LocalDateTime end);
    

}


