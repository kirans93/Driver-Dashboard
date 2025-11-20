package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteSummaryDto {
  
	private String routeNumber;
    private Integer studentsOnboard;
    private Integer capacity;
    private Double distance;          // km
    private String etaSchool;  // expected arrival
}
