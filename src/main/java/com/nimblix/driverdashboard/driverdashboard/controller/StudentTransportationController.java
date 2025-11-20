package com.nimblix.driverdashboard.driverdashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimblix.driverdashboard.driverdashboard.model.StudentTransportation;
import com.nimblix.driverdashboard.driverdashboard.service.StudentTransportationService;

import lombok.RequiredArgsConstructor;

/**
 * NOTE: This is an optional controller.
 * 
 * Provides APIs to manage student transportation records which is not part of Driver Dashboard,
 * including driver-wise and student-wise transport history,
 * along with monthly statistics.
 */
@RestController
@RequestMapping("/api/student-transportation")
@RequiredArgsConstructor
public class StudentTransportationController {

    private final StudentTransportationService transportationService;

    // ========================= POST =========================

    /**
     * API: Create a new student transportation record
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/student-transportation
     * BODY: StudentTransportation JSON
     * RESPONSE: Saved StudentTransportation JSON
     */
    @PostMapping
    public ResponseEntity<StudentTransportation> createTransportation(@RequestBody StudentTransportation transportation) {
        StudentTransportation created = transportationService.createTransportationRecord(transportation);
        return ResponseEntity.ok(created);
    }

    // ========================= GET =========================

    /**
     * API: Fetch all transportation records for a driver (with optional date range)
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/student-transportation/driver/{driverId}?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * PATH PARAM: driverId
     * QUERY PARAMS: startDate (optional), endDate (optional)
     * RESPONSE: List of StudentTransportation JSON
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<StudentTransportation>> getByDriver(
            @PathVariable String driverId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<StudentTransportation> records = transportationService.getTransportationsByDriver(driverId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    /**
     * API: Fetch all transportation records for a student (with optional date range)
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/student-transportation/student/{studentId}?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD
     * PATH PARAM: studentId
     * QUERY PARAMS: startDate (optional), endDate (optional)
     * RESPONSE: List of StudentTransportation JSON
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentTransportation>> getByStudent(
            @PathVariable String studentId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<StudentTransportation> records = transportationService.getTransportationsByStudent(studentId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    /**
     * API: Fetch transportation statistics for a driver for a specific month
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/student-transportation/driver/{driverId}/stats?month=YYYY-MM
     * PATH PARAM: driverId
     * QUERY PARAM: month (optional, format: YYYY-MM)
     * RESPONSE: Map with stats (e.g., total trips, students transported)
     */
    @GetMapping("/driver/{driverId}/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @PathVariable String driverId,
            @RequestParam(required = false) String month
    ) {
        Map<String, Object> stats = transportationService.getTransportationStats(driverId, month);
        return ResponseEntity.ok(stats);
    }
}
