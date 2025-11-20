package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.DashboardSummary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardSummaryRepository extends JpaRepository<DashboardSummary, String> {
	
    Optional<DashboardSummary> findByDriver_Id(String driverId); // ✅

}