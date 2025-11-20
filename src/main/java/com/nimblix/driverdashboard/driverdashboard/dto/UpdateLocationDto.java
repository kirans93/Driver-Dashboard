package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to update the current location and status of a vehicle.
 * All fields except vehicleId and driverId are optional.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationDto {

    private String vehicleId;            // Vehicle unique ID
    private String driverId;             // Driver unique ID
    private String latitude;             // Current latitude from GPS
    private String longitude;            // Current longitude from GPS
    private Double speed;                // Optional: current speed in km/h
    private Boolean isMoving;            // Optional: true if vehicle is moving
    private Double fuelLevel;            // Optional: fuel level in liters or percentage
    private Double engineTemperature;    // Optional: engine temperature in °C
}
