package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyVehicleDashboardDto {
    private VehicleDto vehicleDetails;
    private VehicleStatusDto liveStatus;
    private RouteSummaryDto todayRoute;
}
