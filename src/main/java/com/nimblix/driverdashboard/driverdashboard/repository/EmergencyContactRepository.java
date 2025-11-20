package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, String> {
    List<EmergencyContact> findByDepartment(String department);
    List<EmergencyContact> findByRole(String role);
}