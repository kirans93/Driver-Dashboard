package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLogDto {
    
	private String id;
    private String vehicleId;
    private String driverId;
    private String issueDescription;
    private LocalDateTime reportedAt;
    private String status;
    private LocalDateTime resolvedAt;
    private String resolvedBy;
    private String attachments;
}
