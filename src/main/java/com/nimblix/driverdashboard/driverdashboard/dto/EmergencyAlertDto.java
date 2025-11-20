package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlertDto {
    private String id;
    private String vehicleId;
    private String driverId;
    private LocalDateTime alertTime;
    private Double latitude;
    private Double longitude;
    private String alertType;
    private String status;
}
