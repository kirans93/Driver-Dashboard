package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInfoDto {
    private String engineHealth;   // "Excellent"
    private String fuelLevel;      // "75%"
    private String nextService;    // "Due Tomorrow"
    private String mileageToday;   // "186 km"
    
   
}
