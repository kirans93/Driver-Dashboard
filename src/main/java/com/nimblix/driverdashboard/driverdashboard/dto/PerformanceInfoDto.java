package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceInfoDto {

	private String attendance;        // e.g. "Present"
    private String tripsCompleted;    // e.g. "8/8"
    private String onTimePerformance; // e.g. "100%"
}

