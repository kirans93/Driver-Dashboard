package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelEntryDto {
    private String id;
    private String vehicleId;
    private String driverId;
    private Double fuelAddedLitres;
    private Double fuelCost;
    private Double odometerKm;
    private LocalDateTime createdAt;
    
}
