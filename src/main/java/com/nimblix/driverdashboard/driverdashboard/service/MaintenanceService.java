package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceItem;
import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceSummary;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.exception.OperationFailedException;
import com.nimblix.driverdashboard.driverdashboard.model.MaintenanceItemEntity;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.MaintenanceItemRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleTrackingRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceItemRepository repository;
    private final VehicleTrackingRepository vehicleTrackingRepository;

    public MaintenanceService(MaintenanceItemRepository repository,
                              VehicleTrackingRepository vehicleTrackingRepository) {
        this.repository = repository;
        this.vehicleTrackingRepository = vehicleTrackingRepository;
    }

    /** Add a new maintenance item */
    public MaintenanceItem addMaintenanceItem(MaintenanceItem dto) {
        try {
            MaintenanceItemEntity entity = new MaintenanceItemEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setTitle(dto.getTitle());
            entity.setPriority(dto.getPriority());
            entity.setCategory(dto.getCategory());
            entity.setServiceType(dto.getServiceType());
            entity.setAction(dto.getAction());
            entity.setStatus(dto.getStatus());
            entity.setEstimatedCost(dto.getEstimatedCost());

            // Link VehicleTracking if provided
            if (dto.getVehicleTrackingId() != null) {
                VehicleTracking vt = vehicleTrackingRepository.findById(dto.getVehicleTrackingId())
                        .orElseThrow(() -> new DetailsNotFoundException("VehicleTracking not found"));
                entity.setVehicleTracking(vt);
            }

            MaintenanceItemEntity saved = repository.save(entity);

            return mapToDto(saved);

        } catch (Exception e) {
            throw new OperationFailedException("Failed to save maintenance item: " + e.getMessage());
        }
    }

    /** Get all maintenance items */
    public List<MaintenanceItem> getAllMaintenanceItems() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /** Get summary counts (Urgent / Scheduled / Completed) */
    public MaintenanceSummary getSummary() {
        List<MaintenanceItemEntity> items = repository.findAll();

        long urgent = items.stream()
                .filter(i -> "High".equalsIgnoreCase(i.getPriority())
                          && !"Completed".equalsIgnoreCase(i.getStatus()))
                .count();

        long scheduled = items.stream()
                .filter(i -> "Scheduled".equalsIgnoreCase(i.getStatus()))
                .count();

        long completed = items.stream()
                .filter(i -> "Completed".equalsIgnoreCase(i.getStatus()))
                .count();

        return new MaintenanceSummary((int) urgent, (int) scheduled, (int) completed);
    }

    /** Update an existing maintenance item */
    public MaintenanceItem updateMaintenanceItem(String id, MaintenanceItem dto) {
        MaintenanceItemEntity existing = repository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("Maintenance item not found with id " + id));

        try {
            existing.setTitle(dto.getTitle() != null ? dto.getTitle() : existing.getTitle());
            existing.setPriority(dto.getPriority() != null ? dto.getPriority() : existing.getPriority());
            existing.setCategory(dto.getCategory() != null ? dto.getCategory() : existing.getCategory());
            existing.setServiceType(dto.getServiceType() != null ? dto.getServiceType() : existing.getServiceType());
            existing.setAction(dto.getAction() != null ? dto.getAction() : existing.getAction());
            existing.setStatus(dto.getStatus() != null ? dto.getStatus() : existing.getStatus());
            existing.setEstimatedCost(dto.getEstimatedCost() != null && dto.getEstimatedCost() > 0
                    ? dto.getEstimatedCost() : existing.getEstimatedCost());

            // Update vehicle tracking if provided
            if (dto.getVehicleTrackingId() != null) {
                VehicleTracking vt = vehicleTrackingRepository.findById(dto.getVehicleTrackingId())
                        .orElseThrow(() -> new DetailsNotFoundException("VehicleTracking not found"));
                existing.setVehicleTracking(vt);
            }

            MaintenanceItemEntity updated = repository.save(existing);
            return mapToDto(updated);

        } catch (Exception e) {
            throw new OperationFailedException("Failed to update maintenance item: " + e.getMessage());
        }
    }

    /** Delete a maintenance item */
    public void deleteMaintenanceItem(String id) {
        if (!repository.existsById(id)) {
            throw new DetailsNotFoundException("Maintenance item not found with id: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete maintenance item with id " + id);
        }
    }

    /** Helper to map entity to DTO */
    private MaintenanceItem mapToDto(MaintenanceItemEntity entity) {
        MaintenanceItem dto = new MaintenanceItem();
        dto.setTitle(entity.getTitle());
        dto.setPriority(entity.getPriority());
        dto.setCategory(entity.getCategory());
        dto.setServiceType(entity.getServiceType());
        dto.setAction(entity.getAction());
        dto.setStatus(entity.getStatus());
        dto.setEstimatedCost(entity.getEstimatedCost());
        if (entity.getVehicleTracking() != null) {
            dto.setVehicleTrackingId(entity.getVehicleTracking().getId());
        }
        return dto;
    }
}
