package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class FuelTrendDto {
    private String month;
    private Double fuelLiters;
    private Double cost;
    private Integer distanceKm;
    private Double mileageKmpl;

   
}
