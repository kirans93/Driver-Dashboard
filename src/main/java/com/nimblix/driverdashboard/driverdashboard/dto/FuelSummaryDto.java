package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelSummaryDto {

    private CurrentFuelStatus currentFuelStatus;
    private MonthlyConsumption monthlyConsumption;
    private CostAnalysis costAnalysis;
    private List<FuelLogDto> recentLogs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentFuelStatus {
        private Double percentage;
        private int odometerKm;
        private double estimatedRangeKm;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyConsumption {
        private double totalFuelLiters;
        private double totalCost;
        private double mileageKmpl;
        private int distanceKm;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostAnalysis {
        private String fuelEfficiency;
        private Double changeVsLastMonth;
        private int targetMileage = 12; // default value
    }
}
