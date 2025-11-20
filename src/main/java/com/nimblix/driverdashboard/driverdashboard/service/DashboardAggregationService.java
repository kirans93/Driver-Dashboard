package com.nimblix.driverdashboard.driverdashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.DriverDailyTransportStatsDto;
import com.nimblix.driverdashboard.driverdashboard.dto.DriverFullDashboardDto;
import com.nimblix.driverdashboard.driverdashboard.dto.FuelSummaryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MyVehicleDashboardDto;
import com.nimblix.driverdashboard.driverdashboard.dto.AnnouncementInfoDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceSummary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardAggregationService {

	private final MyVehicleService myVehicleService;
	private final StudentTransportationService transportationService;
	private final FuelService fuelService;
	private final AnnouncementService announcementService;
	private final MaintenanceService maintenanceService;

	public DriverFullDashboardDto getDriverDashboard(String driverId, String vehicleId) {
		MyVehicleDashboardDto vehicle = null;
		if (vehicleId != null) {
			vehicle = myVehicleService.getDashboard(vehicleId);
		}
		DriverDailyTransportStatsDto transport = transportationService.getTodayDriverDailyStats(driverId);
		FuelSummaryDto fuel = fuelService.getFuelSummary(driverId);
		MaintenanceSummary maintenance = maintenanceService.getSummary();
		List<AnnouncementInfoDto> announcements = announcementService.getRecentAnnouncements();
		return new DriverFullDashboardDto(driverId, vehicle, transport, fuel, maintenance, announcements);
	}
}


