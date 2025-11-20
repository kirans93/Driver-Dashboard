package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.SafetyChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface SafetyChecklistRepository extends JpaRepository<SafetyChecklist, String> {

    // ✅ Find all checklists for a given VehicleTracking ID
    List<SafetyChecklist> findByVehicleTracking_Id(String vehicleTrackingId);

    // ✅ Find all checklists with a specific status
    List<SafetyChecklist> findByStatus(String status);

    // ✅ Find checklists by VehicleTracking ID + status
    List<SafetyChecklist> findByVehicleTracking_IdAndStatus(String vehicleTrackingId, String status);
    
    
    
    List<SafetyChecklist> findByVehicleTracking_DriverIdAndCheckTimeBetween(String driverId, LocalTime start, LocalTime end);

}
