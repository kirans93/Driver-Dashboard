package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   
@NoArgsConstructor      
@AllArgsConstructor 

public class DriverInfoDto {
 
	private String department;
    private String driverId;
    private String shift;
    private String vehicleAssigned;
    private String phone;
    
    
    
}
