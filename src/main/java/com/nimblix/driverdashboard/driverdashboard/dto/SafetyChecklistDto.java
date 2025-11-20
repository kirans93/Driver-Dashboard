package com.nimblix.driverdashboard.driverdashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SafetyChecklistDto {
    private String checkItem;

    @JsonFormat(pattern = "HH:mm:ss") // matches checkTime format
    private LocalTime checkTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // matches createdAt format
    private LocalDateTime createdAt;

    private String status;
    private String vehicleTrackingId;
}
