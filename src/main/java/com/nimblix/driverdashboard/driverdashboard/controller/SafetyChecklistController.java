package com.nimblix.driverdashboard.driverdashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimblix.driverdashboard.driverdashboard.dto.SafetyChecklistDto;
import com.nimblix.driverdashboard.driverdashboard.model.SafetyChecklist;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.service.SafetyChecklistService;
import com.nimblix.driverdashboard.driverdashboard.service.VehicleTrackingService;

@RestController
@RequestMapping("/api/safety-checklist")
public class SafetyChecklistController {

    private final SafetyChecklistService service;
    private final VehicleTrackingService vehicleTrackingService;

    
    public SafetyChecklistController(SafetyChecklistService service, VehicleTrackingService vehicleTrackingService) {
		super();
		this.service = service;
		this.vehicleTrackingService = vehicleTrackingService;
	}

	// ========================= GET =========================

    /**
     * API: Fetch all safety checklists
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/safety-checklist
     * RESPONSE: List of SafetyChecklist JSON
     */
    @GetMapping
    public ResponseEntity<List<SafetyChecklist>> list() {
        List<SafetyChecklist> checklists = service.findAll();
        return ResponseEntity.ok(checklists);
    }

    /**
     * API: Fetch safety checklists by vehicleId
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/safety-checklist/vehicle/{vehicleId}
     * PATH PARAM: vehicleId
     * RESPONSE: List of SafetyChecklist JSON
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<SafetyChecklist>> byVehicle(@PathVariable String vehicleId) {
        List<SafetyChecklist> checklists = service.findByVehicleTrackingId(vehicleId);
        return ResponseEntity.ok(checklists);
    }

    /**
     * API: Fetch safety checklists by status
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/safety-checklist/status/{status}
     * PATH PARAM: status
     * RESPONSE: List of SafetyChecklist JSON
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<SafetyChecklist>> byStatus(@PathVariable String status) {
        List<SafetyChecklist> checklists = service.findByStatus(status);
        return ResponseEntity.ok(checklists);
    }

    /**
     * API: Fetch safety checklists by vehicleId and status
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/safety-checklist/vehicle/{vehicleId}/status/{status}
     * PATH PARAMS: vehicleId, status
     * RESPONSE: List of SafetyChecklist JSON
     */
    @GetMapping("/vehicle/{vehicleId}/status/{status}")
    public ResponseEntity<List<SafetyChecklist>> byVehicleAndStatus(
            @PathVariable String vehicleId, @PathVariable String status) {
        List<SafetyChecklist> checklists = service.findByVehicleTrackingIdAndStatus(vehicleId, status);
        return ResponseEntity.ok(checklists);
    }

    /**
     * API: Fetch single safety checklist by ID
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/safety-checklist/{id}
     * PATH PARAM: id
     * RESPONSE: SafetyChecklist JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<SafetyChecklist> get(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ========================= POST =========================

    /**
     * API: Create a new safety checklist
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/safety-checklist
     * BODY: SafetyChecklist JSON
     * RESPONSE: Saved SafetyChecklist JSON
     */
    @PostMapping
    public ResponseEntity<SafetyChecklist> create(@RequestBody SafetyChecklistDto dto) {
        try {
            SafetyChecklist checklist = new SafetyChecklist();
            checklist.setCheckItem(dto.getCheckItem());
            checklist.setStatus(dto.getStatus());
            checklist.setCheckTime(dto.getCheckTime()); // convert to LocalTime if needed
            checklist.setCreatedAt(dto.getCreatedAt()); // convert to LocalDateTime if needed

            // Fetch VehicleTracking entity
            VehicleTracking vt = vehicleTrackingService.findById(dto.getVehicleTrackingId())
                    .orElseThrow(() -> new RuntimeException("VehicleTracking not found"));
            checklist.setVehicleTracking(vt);

            SafetyChecklist saved = service.save(checklist);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // ========================= PUT =========================

    /**
     * API: Update safety checklist status
     * METHOD: PUT
     * FULL URL: http://localhost:8080/api/safety-checklist/{id}/status
     * PATH PARAM: id
     * BODY: {"status": "NEW_STATUS"}
     * RESPONSE: Updated SafetyChecklist JSON
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<SafetyChecklist> updateStatus(
            @PathVariable String id, @RequestBody Map<String, String> statusUpdate) {
        try {
            SafetyChecklist updatedChecklist = service.updateStatus(id, statusUpdate.get("status"));
            return ResponseEntity.ok(updatedChecklist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========================= DELETE =========================

    /**
     * API: Delete safety checklist by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/api/safety-checklist/{id}
     * PATH PARAM: id
     * RESPONSE: { "message": "Checklist deleted successfully" }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Checklist deleted successfully"));
    }
}
