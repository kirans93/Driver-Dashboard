package com.nimblix.driverdashboard.driverdashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.FuelEntry;

@Repository
public interface FuelEntryRepository extends JpaRepository<FuelEntry, String> {
}
