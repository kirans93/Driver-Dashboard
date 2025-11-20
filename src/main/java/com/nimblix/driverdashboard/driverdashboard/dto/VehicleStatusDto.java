package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleStatusDto {
    private String engineTemperature; // e.g., Normal / High
    private Double speed;             // km/h
    private String gpsSignal;         // Strong / Weak
    private Double fuelLevel;         // %
    private Boolean isMoving;
}