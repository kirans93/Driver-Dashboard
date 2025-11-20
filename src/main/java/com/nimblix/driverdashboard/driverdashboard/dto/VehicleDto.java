package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private String id;
    private String vehicleNumber;
    private String model;
    private Integer capacity;
    private LocalDateTime insuranceValidity;
    private String assignedDriverId;
}
