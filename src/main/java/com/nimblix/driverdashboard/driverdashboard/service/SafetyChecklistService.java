package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.model.SafetyChecklist;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.SafetyChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SafetyChecklistService {

    private final SafetyChecklistRepository repository;
    private final VehicleTrackingService vehicleTrackingService; // to fetch VehicleTracking

    // ---------------- FETCH ----------------

    public List<SafetyChecklist> findAll() {
        return repository.findAll();
    }

    public Optional<SafetyChecklist> findById(String id) {
        return repository.findById(id);
    }

    public List<SafetyChecklist> findByVehicleTrackingId(String vehicleTrackingId) {
        return repository.findByVehicleTracking_Id(vehicleTrackingId);
    }

    public List<SafetyChecklist> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<SafetyChecklist> findByVehicleTrackingIdAndStatus(String vehicleTrackingId, String status) {
        return repository.findByVehicleTracking_IdAndStatus(vehicleTrackingId, status);
    }

    // ---------------- CREATE / UPDATE ----------------

    @Transactional
    public SafetyChecklist save(SafetyChecklist checklist) {
        System.out.println("⚡ Saving checklist: " + checklist);
        return repository.save(checklist);
    }

    @Transactional
    public SafetyChecklist updateStatus(String id, String status) {
        SafetyChecklist checklist = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist not found with id: " + id));
        checklist.setStatus(status);
        return repository.save(checklist);
    }

    // ---------------- DELETE ----------------

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Checklist not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // ---------------- HELPER ----------------

    public VehicleTracking getVehicleTrackingById(String vehicleTrackingId) {
        return vehicleTrackingService.findById(vehicleTrackingId)
                .orElseThrow(() -> new RuntimeException("VehicleTracking not found with id: " + vehicleTrackingId));
    }
}
