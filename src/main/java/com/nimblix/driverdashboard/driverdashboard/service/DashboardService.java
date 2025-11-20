package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.model.DashboardSummary;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.repository.DashboardSummaryRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.DriverRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.StudentTransportationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardSummaryRepository dashboardSummaryRepository;
    private final StudentTransportationRepository studentTransportationRepository;
    private final DriverRepository driverRepository;

    // Fetch summary by driver ID
    public Map<String, Object> getSummary(String driverId) {
        if (driverId != null) {
            Optional<DashboardSummary> savedSummary = dashboardSummaryRepository.findByDriver_Id(driverId);
            if (savedSummary.isPresent()) {
                return convertToResponseMap(savedSummary.get());
            }
        }
        return getDefaultSummary(driverId);
    }

    // Create or update summary
    public Map<String, Object> createOrUpdateSummary(Map<String, Object> payload) {
        String driverId = (String) payload.get("driverId");
        if (driverId == null || driverId.isEmpty()) {
            throw new RuntimeException("driverId is required");
        }

        // Fetch Driver entity
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

        // Convert payload to entity
        DashboardSummary summary = convertToEntity(payload, driver);
        DashboardSummary savedSummary = dashboardSummaryRepository.save(summary);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Dashboard summary updated");
        response.put("data", convertToResponseMap(savedSummary));

        return response;
    }

    // Convert payload map to DashboardSummary entity
    private DashboardSummary convertToEntity(Map<String, Object> payload, Driver driver) {
        DashboardSummary summary = new DashboardSummary();
        summary.setDriver(driver);

        Map<String, Object> performanceSummary = (Map<String, Object>) payload.get("performanceSummary");
        Map<String, Object> monthlyStats = (Map<String, Object>) payload.get("monthlyStatistics");
        Map<String, Boolean> achievements = (Map<String, Boolean>) payload.get("recentAchievements");

        if (performanceSummary != null) {
            summary.setOverallRating(convertToDouble(performanceSummary.get("overallRating")));
            summary.setOnTimePerformance((String) performanceSummary.get("onTimePerformance"));
            summary.setSafetyScore((String) performanceSummary.get("safetyScore"));
            summary.setFuelEfficiency((String) performanceSummary.get("fuelEfficiency"));
        }

        if (monthlyStats != null) {
            summary.setTripsCompleted(convertToInteger(monthlyStats.get("tripsCompleted")));
            summary.setKmDriven(convertToInteger(monthlyStats.get("kmDriven")));
            summary.setFuelUsed(convertToInteger(monthlyStats.get("fuelUsed")));
            summary.setStudentsTransported(convertToInteger(monthlyStats.get("studentsTransported")));
        }

        if (achievements != null) {
            summary.setRecentAchievements(achievements);
        }

        return summary;
    }


    // Convert entity to response map
    private Map<String, Object> convertToResponseMap(DashboardSummary summary) {
        Map<String, Object> response = new HashMap<>();

        response.put("driverId", summary.getDriver().getId());
        response.put("driverName", summary.getDriver().getName());

        Map<String, Object> performanceSummary = new HashMap<>();
        performanceSummary.put("overallRating", summary.getOverallRating());
        performanceSummary.put("onTimePerformance", summary.getOnTimePerformance());
        performanceSummary.put("safetyScore", summary.getSafetyScore());
        performanceSummary.put("fuelEfficiency", summary.getFuelEfficiency());
        response.put("performanceSummary", performanceSummary);

        Map<String, Object> monthlyStats = new HashMap<>();
        monthlyStats.put("tripsCompleted", summary.getTripsCompleted());
        monthlyStats.put("kmDriven", summary.getKmDriven());
        monthlyStats.put("fuelUsed", summary.getFuelUsed());
        monthlyStats.put("studentsTransported", summary.getStudentsTransported());
        response.put("monthlyStatistics", monthlyStats);

        response.put("recentAchievements", summary.getRecentAchievements()
            .entrySet().stream()
            .map(e -> Map.of("name", e.getKey(), "completed", e.getValue()))
            .toList());

        return response;
    }


    // Default summary if none exists
    private Map<String, Object> getDefaultSummary(String driverId) {
        Map<String, Object> response = new HashMap<>();

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        Map<String, Object> performanceSummary = new HashMap<>();
        performanceSummary.put("overallRating", 4.9);
        performanceSummary.put("onTimePerformance", "98.5%");
        performanceSummary.put("safetyScore", "100%");
        performanceSummary.put("fuelEfficiency", "Excellent");
        response.put("performanceSummary", performanceSummary);

        Map<String, Object> monthlyStats = new HashMap<>();
        if (driverId != null) {
            monthlyStats.put("tripsCompleted", calculateTripsCompleted(driverId, startOfMonth, endOfMonth));
            monthlyStats.put("kmDriven", calculateKmDriven(driverId));
            monthlyStats.put("fuelUsed", calculateFuelUsed(driverId));
            monthlyStats.put("studentsTransported", calculateStudentsTransported(driverId, startOfMonth, endOfMonth));
        } else {
            monthlyStats.put("tripsCompleted", 380);
            monthlyStats.put("kmDriven", 5625);
            monthlyStats.put("fuelUsed", 450);
            monthlyStats.put("studentsTransported", 890);
        }
        response.put("monthlyStatistics", monthlyStats);

        response.put("recentAchievements", getRecentAchievements());

        return response;
    }

    // Helper methods
    private long calculateStudentsTransported(String driverId, LocalDate startDate, LocalDate endDate) {
        return studentTransportationRepository.countUniqueStudentsTransported(driverId, startDate, endDate);
    }

    private long calculateTripsCompleted(String driverId, LocalDate startDate, LocalDate endDate) {
        return studentTransportationRepository.countTripsCompleted(driverId, startDate, endDate);
    }

    private int calculateKmDriven(String driverId) {
        // Placeholder logic; replace with real calculation
        return 5625;
    }

    private int calculateFuelUsed(String driverId) {
        // Placeholder logic; replace with real calculation
        return 450;
    }

    private List<Map<String, Object>> getRecentAchievements() {
        return List.of(
            Map.of("name", "Perfect Attendance - March 2024", "completed", false),
            Map.of("name", "Safety Excellence Award", "completed", false),
            Map.of("name", "Fuel Efficiency Champion", "completed", false)
        );
    }

    // Conversion helpers
    private Double convertToDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof String) {
            try { return Double.parseDouble((String) value); } 
            catch (NumberFormatException e) { return null; }
        }
        return null;
    }

    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) {
            try { return Integer.parseInt((String) value); } 
            catch (NumberFormatException e) { return null; }
        }
        return null;
    }
}
