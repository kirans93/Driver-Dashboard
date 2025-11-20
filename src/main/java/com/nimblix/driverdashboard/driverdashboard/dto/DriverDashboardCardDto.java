package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDashboardCardDto {
    private String initials;          // RK
    private String fullName;          // Rajesh Kumar
    private String vehicleAndRoute;   // Bus #15 • Route A
    private String experience;        // 5 years
    private String dutyStatus;        // On Duty / Off Duty
    private String maintenanceStatus; // Due / Ok
    private String licenseStatus;     // Valid / Expired
    private String phone;             // +91-98765-43210
}
