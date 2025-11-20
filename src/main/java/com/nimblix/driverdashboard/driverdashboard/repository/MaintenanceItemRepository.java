package com.nimblix.driverdashboard.driverdashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimblix.driverdashboard.driverdashboard.model.MaintenanceItemEntity;

public interface MaintenanceItemRepository extends JpaRepository<MaintenanceItemEntity, String> {
	
}
