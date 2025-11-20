package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteStopDto {
    private String id;
    private String stopName;
    private LocalTime scheduledTime;
    private Integer studentCount;
    private String status;
    private String busRouteId;
}
