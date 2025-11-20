package com.nimblix.driverdashboard.driverdashboard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartRouteDto {
	
	@NotNull(message = "vehicleId is required")
    private String vehicleId;
	
	@NotNull(message = "routeId is required")
    private String routeId;
   
    @NotNull(message = "driverId is required")
    private String driverId;
    
    private String latitude;
    private String longitude;    
}
