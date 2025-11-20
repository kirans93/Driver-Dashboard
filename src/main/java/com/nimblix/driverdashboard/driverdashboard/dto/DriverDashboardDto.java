package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DriverDashboardDto {

    private String driverId;
    private String driverName;
    private Map<String, Object> performanceSummary;
    private List<AchievementDto> recentAchievements;
    private Map<String, Object> monthlyStatistics;

    @Data
    @AllArgsConstructor
    public static class AchievementDto {
        private String name;
        private boolean completed;
    }
}
