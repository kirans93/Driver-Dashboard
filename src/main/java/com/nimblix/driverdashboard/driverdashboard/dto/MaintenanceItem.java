package com.nimblix.driverdashboard.driverdashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class MaintenanceItem {
    private String title;
    private String priority;
    private String category;
    private String serviceType;
    private String action;
    private String status;       // ✅
    
    @JsonProperty("estimated_cost")
    private Double estimatedCost; // ✅
    
    @JsonProperty("vehical_tracking_id")
    private String vehicleTrackingId;


    
    
}
