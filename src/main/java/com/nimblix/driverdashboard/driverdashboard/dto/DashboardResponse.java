package com.nimblix.driverdashboard.driverdashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   
@NoArgsConstructor      
@AllArgsConstructor 
public class DashboardResponse {
    private List<MaintenanceSummary> summaries;
    private List<MaintenanceItem> maintenanceSchedule;
  
   
}