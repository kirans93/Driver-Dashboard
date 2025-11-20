package com.nimblix.driverdashboard.driverdashboard.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.FuelLog;
@Repository
public interface FuelRepository extends JpaRepository<FuelLog, String> {

    // Recent 5 logs by driver ID
    List<FuelLog> findTop5ByDriverIdOrderByDateDesc(String driverId);

    // Logs for a driver between dates
    List<FuelLog> findByDriverAndDateBetweenOrderByDateAsc(Driver driver, Timestamp start, Timestamp end);

    // Top 5 recent logs for a driver
    List<FuelLog> findTop5ByDriverOrderByDateDesc(Driver driver);

    // **Add this for previous log calculation**
    Optional<FuelLog> findTopByDriverAndDateBeforeOrderByDateDesc(Driver driver, Timestamp date);
    
    Optional<FuelLog> findTopByDriverOrderByDateDesc(Driver driver);

    

    List<FuelLog> findByDriver_IdAndDateBetweenOrderByDateAsc(
        String driverId, Timestamp start, Timestamp end
    );

    
    
}
