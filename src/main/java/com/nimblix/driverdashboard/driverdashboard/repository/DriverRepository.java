package com.nimblix.driverdashboard.driverdashboard.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimblix.driverdashboard.driverdashboard.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, String> {

	Optional<Driver> findByEmployeeId(String employeeId);
	Optional<Driver> findByVehicleAssigned(String vehicleNumber);
	
	 // fetch drivers by admin
    List<Driver> findByAdminId(String adminId);
    
    // optional: search by duty status
    List<Driver> findByDutyStatus(String dutyStatus);
    
    @Query("SELECT d FROM Driver d WHERE d.licenseExpiryDate < :threshold")
    List<Driver> findDriversWithExpiringLicenses(@Param("threshold") LocalDateTime threshold);
    
    @Query("SELECT d.name FROM Driver d WHERE d.id = :id")
    String findNameById(@Param("id") String id);


}
