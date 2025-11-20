package com.nimblix.driverdashboard.driverdashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimblix.driverdashboard.driverdashboard.dto.OverviewResponse;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.service.OverviewService;

@RestController
@RequestMapping("/api/non-teaching-staff")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    // ========================= GET =========================

    /**
     * API: Get driver overview by driverId
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/{driverId}/overview
     * PATH PARAM: driverId
     * RESPONSE: OverviewResponse JSON
     */
    @GetMapping("/{driverId}/overview")
    public ResponseEntity<OverviewResponse> getDriverOverview(@PathVariable String driverId) {
        return ResponseEntity.ok(overviewService.getDriverOverview(driverId));
    }

    /**
     * API: Get driver overview by employeeId
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/employee/{employeeId}/overview
     * PATH PARAM: employeeId
     * RESPONSE: OverviewResponse JSON
     */
    @GetMapping("/employee/{employeeId}/overview")
    public ResponseEntity<OverviewResponse> getDriverOverviewByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(overviewService.getDriverOverviewByEmployeeId(employeeId));
    }

    /**
     * API: Get driver overview by vehicle number
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/vehicle/{vehicleNumber}/overview
     * PATH PARAM: vehicleNumber
     * RESPONSE: OverviewResponse JSON
     */
    @GetMapping("/vehicle/{vehicleNumber}/overview")
    public ResponseEntity<OverviewResponse> getDriverOverviewByVehicle(@PathVariable String vehicleNumber) {
        return ResponseEntity.ok(overviewService.getDriverOverviewByVehicle(vehicleNumber));
    }

    /**
     * API: Health check for driver overview module
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/health
     * RESPONSE: Success message as String
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Driver Overview module is healthy ✅");
    }

    // ========================= POST =========================

    /**
     * API: Save new vehicle tracking record
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/add-vehicle-tracking
     * BODY: VehicleTracking JSON
     * RESPONSE: Success message as String
     */
    @PostMapping("/add-vehicle-tracking")
    public ResponseEntity<String> createVehicleTracking(@RequestBody VehicleTracking vehicleTracking) {
        overviewService.saveVehicleTracking(vehicleTracking);
        return ResponseEntity.ok("✅ Vehicle tracking successfully added!");
    }


    /**
     * API: Save new driver
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/add-driver
     * BODY: Driver JSON
     * RESPONSE: Success message as String
     */
    @PostMapping("/add-driver")
    public ResponseEntity<String> createDriver(@RequestBody Driver driver) {
        overviewService.saveDriver(driver);
        return ResponseEntity.ok("✅ Driver successfully added!");
    }

    // ========================= PUT =========================

    /**
     * API: Update vehicle tracking by ID
     * METHOD: PUT
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/vehicle-tracking/{id}
     * PATH PARAM: id (UUID)
     * BODY: VehicleTracking JSON
     * RESPONSE: Success message as String
     */
    @PutMapping("/vehicle-tracking/{id}")
    public ResponseEntity<String> updateVehicleTracking(
            @PathVariable String id,
            @RequestBody VehicleTracking vehicleTracking) {
        overviewService.updateVehicleTracking(id, vehicleTracking);
        return ResponseEntity.ok("✅ Vehicle tracking successfully updated!");
    }

    /**
     * API: Update driver by ID
     * METHOD: PUT
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/drivers/{id}
     * PATH PARAM: id
     * BODY: Driver JSON
     * RESPONSE: Success message as String
     */
    @PutMapping("/drivers/{id}")
    public ResponseEntity<String> updateDriver(
            @PathVariable String id,
            @RequestBody Driver driver) {
        overviewService.updateDriver(id, driver);
        return ResponseEntity.ok("✅ Driver successfully updated!");
    }

    // ========================= DELETE =========================

    /**
     * API: Delete vehicle tracking by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/vehicle-tracking/{id}
     * PATH PARAM: id (UUID)
     * RESPONSE: Success message as String
     */
    @DeleteMapping("/vehicle-tracking/{id}")
    public ResponseEntity<String> deleteVehicleTracking(@PathVariable String id) {
        overviewService.deleteVehicleTracking(id);
        return ResponseEntity.ok("🗑️ Vehicle tracking successfully deleted!");
    }
    
    
//    {
//    "vehicleId": "9d19a4db-10b5-4c45-b050-e115f1aee287",
//    "driverId": "4d36cf98-3a36-4e1e-b84a-0c73669e3703",
//    "vehicleNumber": "KA01AB1234",
//    "routeNumber": "R12",
//    "currentKm": 82350,
//    "fuelLevel": 68,
//    "latitude": "12.9716",
//    "longitude": "77.5946",
//    "speed": "54",
//    "engineStatus": "ON",
//    "engineTemperature": "87.5",
//    "gpsSignalStrength": "STRONG",
//    "isMoving": true,
//    "lastMaintenanceKm": 80000,
//    "nextMaintenanceDue": 85000,
//    "emergencyAlert": "NO",
//    "timestamp": "2025-09-29T18:20:00",
//    "model": "Tata Starbus Ultra",
//    "capacity": 45,
//    "insuranceValidUntil": "2026-03-15"
//  }


    /**
     * API: Delete driver by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/api/non-teaching-staff/drivers/drivers/{id}
     * PATH PARAM: id
     * RESPONSE: Success message as String
     */
    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable String id) {
        overviewService.deleteDriver(id);
        return ResponseEntity.ok("🗑️ Driver successfully deleted!");
    }
    
    
//    {
//
//    	  "name": "ramesh",
//    	  "employeeId": "EMP432",
//    	  "department": "Transport",
//    	  "designation": "Driver controller",
//    	  "shift": "evening",
//    	  "phone": "95656552121",
//    	  "vehicleAssigned": "R022",
//    	  "joiningDate": "2025-09-29T14:55:17.311Z",
//    	  "user": {
//    	   
//    	    "username": "kiransS",
//    	    "password": "lllll",
//    	    "email": "k@gmail.com",
//    	    "role": "driver",
//    	    "isActive": true,
//    	    "createdAt": "2025-09-29T14:55:17.311Z",
//    	    "updatedAt": "2025-09-29T14:55:17.311Z"
//    	  },
//    	  "admin": {
//    	    "id": "111e8400-e29b-41d4-a716-446655440001"
//    	  },
//    	  "duty_status": "doing",
//    	  "experience": 20,
//    	  "license_expiry_date": "2025-09-29T14:55:17.311Z"
//    	}


    // ========================= ERROR HANDLING =========================

    /**
     * API: Global Runtime Exception Handler
     * METHOD: Auto triggered when RuntimeException occurs
     * RESPONSE: Error message as String
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body("❌ Error: " + ex.getMessage());
    }
}
