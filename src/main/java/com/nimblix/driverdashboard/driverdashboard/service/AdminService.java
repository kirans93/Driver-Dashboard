package com.nimblix.driverdashboard.driverdashboard.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.DriverDashboardCardDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.DriverRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleTrackingRepository;

@Service
public class AdminService {

    private final DriverRepository driverRepository;
    private final VehicleTrackingRepository vehicleTrackingRepository;

    public AdminService(DriverRepository driverRepository,
                        VehicleTrackingRepository vehicleTrackingRepository) {
        this.driverRepository = driverRepository;
        this.vehicleTrackingRepository = vehicleTrackingRepository;
    }

    /**
     * Fetch all drivers for admin dashboard view
     */
    public List<DriverDashboardCardDto> getDriverCards(String adminId) {
        // Convert UUID to String because DB stores it as VARCHAR
        List<Driver> drivers = driverRepository.findByAdminId(adminId.toString());

        if (drivers.isEmpty()) {
            throw new DetailsNotFoundException("No drivers found for admin: " + adminId);
        }

        List<DriverDashboardCardDto> cards = new ArrayList<>();

        for (Driver driver : drivers) {
            // Fetch the latest vehicle tracking record
            VehicleTracking vehicle = vehicleTrackingRepository
                    .findTopByDriverIdOrderByTimestampDesc(driver.getId())
                    .orElse(null);

            cards.add(mapToDto(driver, vehicle));
        }

        return cards;
    }

    /**
     * Maps Driver and VehicleTracking entity to DriverDashboardCardDto
     */
    private DriverDashboardCardDto mapToDto(Driver driver, VehicleTracking vehicle) {
        // Compute initials (max 2 letters)
        String initials = Arrays.stream(driver.getName().split(" "))
                .filter(s -> !s.isBlank())
                .map(s -> s.substring(0, 1).toUpperCase())
                .limit(2)
                .collect(Collectors.joining());

        // Vehicle & Route
        String vehicleAndRoute = driver.getVehicleAssigned() != null ? driver.getVehicleAssigned() : "N/A";
        if (vehicle != null && vehicle.getRouteNumber() != null) {
            vehicleAndRoute += " • " + vehicle.getRouteNumber();
        }

        // Experience
        String experience = driver.getExperience() != null ? driver.getExperience() + " years" : "N/A";

        // Maintenance Status
        String maintenanceStatus = Optional.ofNullable(vehicle)
                .map(v -> v.getNextMaintenanceDue() != null && v.getNextMaintenanceDue() <= v.getCurrentKm() ? "Due" : "Ok")
                .orElse("Unknown");

        // License Status
        String licenseStatus = driver.getLicenseExpiryDate() != null ? "Valid" : "Expired";

        return new DriverDashboardCardDto(
                initials,
                driver.getName(),
                vehicleAndRoute,
                experience,
                driver.getDutyStatus() != null ? driver.getDutyStatus() : "Unknown",
                maintenanceStatus,
                licenseStatus,
                driver.getPhone() != null ? driver.getPhone() : "N/A"
        );
    }
}
