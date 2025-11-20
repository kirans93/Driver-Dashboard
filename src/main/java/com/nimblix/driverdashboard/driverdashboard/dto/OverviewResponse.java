package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class OverviewResponse {
    
	 private DriverInfoDto driverInfo = new DriverInfoDto();
	    
	    private VehicleInfoDto vehicleInfo = new VehicleInfoDto();

  private PerformanceInfoDto performanceInfo = new PerformanceInfoDto();
    
}
