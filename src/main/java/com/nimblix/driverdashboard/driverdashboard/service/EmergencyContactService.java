package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.model.EmergencyContact;
import com.nimblix.driverdashboard.driverdashboard.repository.EmergencyContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {
    private final EmergencyContactRepository repository;

    public List<EmergencyContact> findAll() {
        return repository.findAll();
    }

    public Optional<EmergencyContact> findById(String id) {
        return repository.findById(id);
    }

    public EmergencyContact save(EmergencyContact contact) {
        return repository.save(contact);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<EmergencyContact> findByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public List<EmergencyContact> findByRole(String role) {
        return repository.findByRole(role);
    }
}