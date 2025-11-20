package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.ChecklistImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistImageRepository extends JpaRepository<ChecklistImage, String> {
    List<ChecklistImage> findByChecklistId(String checklistId);
    void deleteByChecklistId(String checklistId);
	List<ChecklistImage> findBySafetyChecklistId(String checklistId);
}