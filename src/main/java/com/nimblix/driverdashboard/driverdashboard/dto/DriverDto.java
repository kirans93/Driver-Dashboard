package com.nimblix.driverdashboard.driverdashboard.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

    private String id;
    private String name;
    private String employeeId;
    private String department;
    private String designation;
    private String shift;
    private String phone;
    private String vehicleAssigned;
    private LocalDateTime joiningDate;

    // --- User Info ---
    private String userId;          // optional if updating
    private String username;
    private String password;
    private String email;
    private String role;            // STUDENT, STAFF, ADMIN, etc.
    private Boolean isActive;       // optional, default true

    // --- Other related entities ---
    private String adminId;          // ID of Admin
    private String announcementId;   // ID of Announcement

    // --- Driver-specific fields ---
    private LocalDateTime licenseExpiryDate;
    private String dutyStatus;       // "On Duty", "Off Duty"
    private Integer experience;
}
