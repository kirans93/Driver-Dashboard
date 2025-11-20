package com.nimblix.driverdashboard.driverdashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripProgressDto {
    private String nextStop;
    private double distanceCovered;
    private double distanceRemaining;
    private List<RouteStopDto> stops;

    
}
