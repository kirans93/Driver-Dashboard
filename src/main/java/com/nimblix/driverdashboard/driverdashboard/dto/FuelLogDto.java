package com.nimblix.driverdashboard.driverdashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                   
@NoArgsConstructor     
@AllArgsConstructor 

public class FuelLogDto {
  
	public FuelLogDto(String id2, String driverId2, Object object, String station2, Double liters2, Double cost2,
			Integer odometerKm2) {
		// TODO Auto-generated constructor stub
	}

	@JsonProperty("id")
    private String id;

    @JsonProperty("driver_id")
    private String driverId;

    @JsonProperty("date")
    private String date;  // keep as String if parsing later

    @JsonProperty("station")
    private String station;

    @JsonProperty("liters")
    private Double liters;

    @JsonProperty("cost")
    private Double cost;

    @JsonProperty("odometer_km")
    private Integer odometerKm;
   

    
}
