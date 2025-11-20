package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UpdateVehicleDto {
    private String model;
    private Integer capacity;
    private LocalDateTime insuranceValidity;
    private String assignedDriverId; // optional
}
