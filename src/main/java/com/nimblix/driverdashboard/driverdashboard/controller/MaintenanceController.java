package com.nimblix.driverdashboard.driverdashboard.controller;

import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceItem;
import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceSummary;
import com.nimblix.driverdashboard.driverdashboard.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // ========================= POST =========================

    /**
     * API: Add new maintenance item
     * METHOD: POST
     * FULL URL: http://localhost:8080/driver/maintenance/add
     * BODY: MaintenanceItem JSON
     * RESPONSE: Success message as String
     */
   
    
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody MaintenanceItem item) {
        maintenanceService.addMaintenanceItem(item);
        return ResponseEntity.ok("✅ Successfully added maintenance item");
    }


    // ========================= GET =========================

    /**
     * API: Fetch all maintenance items
     * METHOD: GET
     * FULL URL: http://localhost:8080/driver/maintenance/all
     * RESPONSE: List of MaintenanceItem objects
     */
    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceItem>> getAllItems() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceItems());
    }

    /**
     * API: Fetch maintenance summary (Urgent / Scheduled / Completed)
     * METHOD: GET
     * FULL URL: http://localhost:8080/driver/maintenance/summary
     * RESPONSE: MaintenanceSummary JSON
     */
    @GetMapping("/summary")
    public ResponseEntity<MaintenanceSummary> getSummary() {
        return ResponseEntity.ok(maintenanceService.getSummary());
    }

    // ========================= PUT =========================

    /**
     * API: Update a maintenance item by ID
     * METHOD: PUT
     * FULL URL: http://localhost:8080/driver/maintenance/{id}
     * PATH PARAM: id
     * BODY: MaintenanceItem JSON
     * RESPONSE: Success message as String
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(
            @PathVariable String id,
            @RequestBody MaintenanceItem item) {
        maintenanceService.updateMaintenanceItem(id, item);
        return ResponseEntity.ok("✅ Successfully updated maintenance item with ID: " + id);
    }

    // ========================= DELETE =========================

    /**
     * API: Delete a maintenance item by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/driver/maintenance/{id}
     * PATH PARAM: id
     * RESPONSE: Success message as String
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        maintenanceService.deleteMaintenanceItem(id);
        return ResponseEntity.ok("✅ Successfully deleted maintenance item with ID: " + id);
    }

    // ========================= EXCEPTION HANDLING =========================

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
