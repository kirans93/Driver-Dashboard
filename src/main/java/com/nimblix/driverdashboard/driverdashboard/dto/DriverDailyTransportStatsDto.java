package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDailyTransportStatsDto {

	private String driverId;
	private String date; // ISO-8601 (YYYY-MM-DD)
	private Integer totalAssignedStudents;
	private Integer totalPickedUp;
	private Integer totalDroppedOff;
	private Integer pendingPickup;
	private Integer pendingDrop;
}


