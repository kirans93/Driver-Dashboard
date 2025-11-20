package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MyVehicalMaintenanceItemRepository extends JpaRepository<MaintenanceLog, UUID> {
}
