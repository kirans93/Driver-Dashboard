package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class MaintenanceSummary {
    private int urgentCount;
    private int scheduledCount;
    private int completedCount;

    
}
