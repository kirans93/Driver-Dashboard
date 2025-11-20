package com.nimblix.driverdashboard.driverdashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverFullDashboardDto {

	private String driverId;

	private MyVehicleDashboardDto vehicle; // details + live status + today route

	private DriverDailyTransportStatsDto transportStatsToday;

	private FuelSummaryDto fuelSummary;

	private MaintenanceSummary maintenanceSummary;

	private List<AnnouncementInfoDto> announcements;
}


