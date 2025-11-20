package com.nimblix.driverdashboard.driverdashboard.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.nimblix.driverdashboard.driverdashboard.dto.DriverDashboardCardDto;
import com.nimblix.driverdashboard.driverdashboard.service.AdminService;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * API: Get all drivers for a given admin
     * METHOD: GET
     * FULL URL: http://localhost:8080/admin/{adminId}/drivers
     * RESPONSE: List of DriverDashboardCardDto
     */
    @GetMapping("/admin/{adminId}/drivers")
    public List<DriverDashboardCardDto> getDriversForAdmin(@PathVariable String adminId) {
        return adminService.getDriverCards(adminId);
    }
}



//INSERT INTO admins (
//    id,
//    employee_id,
//    name,
//    permissions,
//    phone,
//    user_id
//) VALUES (
//    UUID(),                       -- auto-generate UUID for id
//    'EMP001',                     -- employee_id
//    'Kiran S',                    -- name
//    '["ALL"]',                    -- permissions (valid JSON array)
//    '9876543210',                 -- phone
//    'e32a3692-9db4-11f0-8b95-c0bfbec743c4' -- user_id
//);


//INSERT INTO users (
//	    id,
//	    created_at,
//	    updated_at,
//	    email,
//	    username,
//	    password,
//	    role,vehicles
//	    is_active
//	) VALUES (
//	    UUID(),                        -- auto-generate UUID
//	    NOW(),                          -- created_at as current timestamp
//	    NOW(),                          -- updated_at as current timestamp
//	    'kiran@example.com',            -- email
//	    'kiran',                        -- username
//	    'securepassword123',            -- password (store hashed in real use)
//	    'ADMIN',                        -- role
//	    true                            -- is_active
//	);

