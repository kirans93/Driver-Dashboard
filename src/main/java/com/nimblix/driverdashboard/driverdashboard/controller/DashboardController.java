package com.nimblix.driverdashboard.driverdashboard.controller;

import com.nimblix.driverdashboard.driverdashboard.model.StudentTransportation;
import com.nimblix.driverdashboard.driverdashboard.dto.DriverDailyTransportStatsDto;
import com.nimblix.driverdashboard.driverdashboard.service.DashboardService;
import com.nimblix.driverdashboard.driverdashboard.service.StudentTransportationService;
import com.nimblix.driverdashboard.driverdashboard.service.DashboardAggregationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;
    private final StudentTransportationService transportationService;
    private final DashboardAggregationService aggregationService;

    public DashboardController(DashboardService dashboardService,
                               StudentTransportationService transportationService,
                               DashboardAggregationService aggregationService) {
        this.dashboardService = dashboardService;
        this.transportationService = transportationService;
        this.aggregationService = aggregationService;
    }

    // ========================= DASHBOARD ENDPOINTS =========================

    /**
     * API: Get dashboard summary
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/dashboard/summary?driverId={driverId}
     * QUERY PARAMS: Optional -> driverId
     * RESPONSE: Map<String, Object> containing dashboard summary details
     */
    @GetMapping("/dashboard/summary")
    public Map<String, Object> getDashboardSummary(@RequestParam(required = false) String driverId) {
        return dashboardService.getSummary(driverId);
    }

    /**
     * API: Create or update dashboard summary
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/dashboard/summary
     * BODY: JSON object containing dashboard summary payload
     * RESPONSE: Map<String, Object> containing updated/created summary details
     */
    @PostMapping("/dashboard/summary")
    public Map<String, Object> createOrUpdateDashboardSummary(@RequestBody Map<String, Object> payload) {
        return dashboardService.createOrUpdateSummary(payload);
    }

    // ========================= TRANSPORTATION ENDPOINTS =========================

    /**
     * API: Get today's pickup/drop stats for a driver
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/dashboard/daily-transport?driverId={driverId}
     * QUERY PARAMS: driverId (required)
     * RESPONSE: DriverDailyTransportStatsDto
     */
    @GetMapping("/dashboard/daily-transport")
    public DriverDailyTransportStatsDto getDriverDailyTransport(@RequestParam String driverId) {
        return transportationService.getTodayDriverDailyStats(driverId);
    }

    // ========================= FULL DASHBOARD =========================

    /**
     * API: Get a full driver dashboard aggregate
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/dashboard/full?driverId={driverId}&vehicleId={vehicleId}
     * QUERY PARAMS: driverId (required), vehicleId (optional)
     * RESPONSE: DriverFullDashboardDto
     */
    @GetMapping("/dashboard/full")
    public com.nimblix.driverdashboard.driverdashboard.dto.DriverFullDashboardDto getFullDashboard(
            @RequestParam String driverId,
            @RequestParam(required = false) String vehicleId
    ) {
        return aggregationService.getDriverDashboard(driverId, vehicleId);
    }

    /**
     * API: Create a new student transportation record
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/transportation
     * BODY: JSON object containing student transportation details
     * RESPONSE: The created StudentTransportation object
     */
    @PostMapping("/transportation")
    public StudentTransportation createTransportationRecord(@RequestBody StudentTransportation transportation) {
        return transportationService.createTransportationRecord(transportation);
    }

    /**
     * API: Get transportation records by driver
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/transportation/driver/{driverId}?startDate={startDate}&endDate={endDate}
     * PATH PARAM: driverId
     * QUERY PARAMS: Optional -> startDate, endDate
     * RESPONSE: List of StudentTransportation objects for the given driver
     */
    @GetMapping("/transportation/driver/{driverId}")
    public List<StudentTransportation> getTransportationsByDriver(
            @PathVariable String driverId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return transportationService.getTransportationsByDriver(driverId, startDate, endDate);
    }

    /**
     * API: Get transportation records by student
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/transportation/student/{studentId}?startDate={startDate}&endDate={endDate}
     * PATH PARAM: studentId
     * QUERY PARAMS: Optional -> startDate, endDate
     * RESPONSE: List of StudentTransportation objects for the given student
     */
    @GetMapping("/transportation/student/{studentId}")
    public List<StudentTransportation> getTransportationsByStudent(
            @PathVariable String studentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return transportationService.getTransportationsByStudent(studentId, startDate, endDate);
    }

    /**
     * API: Get transportation statistics for a driver
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/transportation/stats/{driverId}?month={month}
     * PATH PARAM: driverId
     * QUERY PARAM: Optional -> month
     * RESPONSE: Map<String, Object> containing transportation stats for the driver
     */
    @GetMapping("/transportation/stats/{driverId}")
    public Map<String, Object> getTransportationStats(
            @PathVariable String driverId,
            @RequestParam(required = false) String month) {
        return transportationService.getTransportationStats(driverId, month);
    }
}
