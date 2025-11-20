package com.nimblix.driverdashboard.driverdashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimblix.driverdashboard.driverdashboard.dto.AddVehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.EmergencyAlertDto;
import com.nimblix.driverdashboard.driverdashboard.dto.FuelEntryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceLogDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MyVehicleDashboardDto;
import com.nimblix.driverdashboard.driverdashboard.dto.RouteSummaryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.StartRouteDto;
import com.nimblix.driverdashboard.driverdashboard.dto.UpdateLocationDto;
import com.nimblix.driverdashboard.driverdashboard.dto.UpdateVehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.VehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.VehicleStatusDto;
import com.nimblix.driverdashboard.driverdashboard.model.Vehicle;
import com.nimblix.driverdashboard.driverdashboard.service.MyVehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/my-vehicle")
@RequiredArgsConstructor
public class MyVehicleController {

    private final MyVehicleService myVehicleService;
    
    
    /**
     * API: Add a new vehicle
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/add-vehicle
     * BODY: AddVehicleDto JSON
     * RESPONSE: Vehicle JSON
     */
    @PostMapping("/add-vehicle")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody AddVehicleDto dto) {
        return ResponseEntity.ok(myVehicleService.addVehicle(dto));
    }

    
    /**
     * API: Update an existing vehicle
     * METHOD: PUT
     * FULL URL: http://localhost:8080/api/my-vehicle/update-vehicle/{vehicleId}
     * PATH PARAM: vehicleId
     * BODY: UpdateVehicleDto JSON
     * RESPONSE: Vehicle JSON
     */
    @PutMapping("/update-vehicle/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String vehicleId,
                                                 @RequestBody UpdateVehicleDto dto) {
        return ResponseEntity.ok(myVehicleService.updateVehicle(vehicleId, dto));
    }

    
    // ========================= Dashboard =========================

    /**
     * API: Get vehicle dashboard data
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/my-vehicle/dashboard/{vehicleId}
     * PATH PARAM: vehicleId
     * RESPONSE: MyVehicleDashboardDto JSON
     */
    @GetMapping("/dashboard/{vehicleId}")
    public ResponseEntity<MyVehicleDashboardDto> getDashboard(@PathVariable String vehicleId) {
        return ResponseEntity.ok(myVehicleService.getDashboard(vehicleId));
    }

    // ========================= Vehicle Details =========================

    /**
     * API: Get vehicle details by ID
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/my-vehicle/details/{vehicleId}
     * PATH PARAM: vehicleId
     * RESPONSE: VehicleDto JSON
     */
    @GetMapping("/details/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicleDetails(@PathVariable String vehicleId) {
        return ResponseEntity.ok(myVehicleService.getVehicleDetails(vehicleId));
    }

    /**
     * API: Get live vehicle status
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/my-vehicle/live-status/{vehicleId}
     * PATH PARAM: vehicleId
     * RESPONSE: VehicleStatusDto JSON
     */
    @GetMapping("/live-status/{vehicleId}")
    public ResponseEntity<VehicleStatusDto> getLiveStatus(@PathVariable String vehicleId) {
        return ResponseEntity.ok(myVehicleService.getLiveStatus(vehicleId));
    }

    /**
     * API: Get today’s route for a vehicle
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/my-vehicle/todays-route/{vehicleId}
     * PATH PARAM: vehicleId
     * RESPONSE: RouteSummaryDto JSON
     */
    @GetMapping("/todays-route/{vehicleId}")
    public ResponseEntity<RouteSummaryDto> getTodaysRoute(@PathVariable String vehicleId) {
        return ResponseEntity.ok(myVehicleService.getTodaysRoute(vehicleId));
    }

    // ========================= Start Route =========================

    /**
     * API: Start a new route for a vehicle
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/start-route
     * BODY: StartRouteDto JSON
     * RESPONSE: Success message as String
     */
    @PostMapping("/start-route")
    public ResponseEntity<String> startRoute(@Valid @RequestBody StartRouteDto dto) {
        return ResponseEntity.ok(myVehicleService.startRoute(dto));
    }

    /**
     * API: Update vehicle location
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/update-location
     * BODY: UpdateLocationDto JSON
     * RESPONSE: Success message as String
     */
    @PostMapping("/update-location")
    public ResponseEntity<String> updateLocation(@RequestBody UpdateLocationDto dto) {
        myVehicleService.updateLocation(dto);
        return ResponseEntity.ok("✅ Location updated successfully");
    }

    // ========================= Fuel Entry =========================

    /**
     * API: Record a new fuel entry
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/fuel-entry
     * BODY: FuelEntryDto JSON
     * RESPONSE: FuelEntryDto JSON
     */
    @PostMapping("/fuel-entry")
    public ResponseEntity<FuelEntryDto> recordFuelEntry(@RequestBody FuelEntryDto dto) {
        return ResponseEntity.ok(myVehicleService.recordFuelEntry(dto));
    }

    // ========================= Maintenance Log =========================

    /**
     * API: Log a maintenance issue for a vehicle
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/log-issue
     * BODY: MaintenanceLogDto JSON
     * RESPONSE: MaintenanceLogDto JSON
     */
    @PostMapping("/log-issue")
    public ResponseEntity<MaintenanceLogDto> logIssue(@RequestBody MaintenanceLogDto dto) {
        return ResponseEntity.ok(myVehicleService.logIssue(dto));
    }

    // ========================= Emergency Alert =========================

    /**
     * API: Send an emergency alert
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/my-vehicle/emergency
     * BODY: EmergencyAlertDto JSON
     * RESPONSE: EmergencyAlertDto JSON
     */
    @PostMapping("/emergency")
    public ResponseEntity<EmergencyAlertDto> sendEmergency(@RequestBody EmergencyAlertDto dto) {
        return ResponseEntity.ok(myVehicleService.sendEmergency(dto));
    }
}
