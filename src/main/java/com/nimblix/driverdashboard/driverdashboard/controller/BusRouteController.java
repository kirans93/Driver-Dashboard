package com.nimblix.driverdashboard.driverdashboard.controller;

import java.util.List;

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

import com.nimblix.driverdashboard.driverdashboard.dto.RouteStopDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.exception.OperationFailedException;
import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.service.BusRouteService;

@RestController
@RequestMapping("/api/bus-routes")
public class BusRouteController {

    private final BusRouteService busRouteService;
	private final BusRouteService routeStopService;

    public BusRouteController(BusRouteService busRouteService, BusRouteService routeStopService) {
		
		this.busRouteService = busRouteService;
		this.routeStopService = routeStopService;
	}

    // ---------------- GET ----------------

    /**
     * API: Get all bus routes
     * METHOD: GET
     * URL: http://localhost:8080/api/bus-routes
     * PURPOSE: Retrieve all bus routes available in the system
     */
    @GetMapping
    public ResponseEntity<List<BusRoute>> getAllBusRoutes() {
        return ResponseEntity.ok(busRouteService.getAllBusRoutes());
    }

    /**
     * API: Get bus route by ID
     * METHOD: GET
     * URL: http://localhost:8080/api/non-teaching-staff/bus-routes/{id}
     * PURPOSE: Retrieve a specific bus route by its unique ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusRoute> getBusRouteById(@PathVariable String id) {
        return ResponseEntity.ok(busRouteService.getBusRouteById(id));
    }

    // ---------------- POST ----------------

    /**
     * API: Add a new bus route
     * METHOD: POST
     * URL: http://localhost:8080/api/bus-routes
     * PURPOSE: Create and save a new bus route
     */
    @PostMapping
    public ResponseEntity<String> createBusRoute(@RequestBody BusRoute busRoute) {
        busRouteService.saveBusRoute(busRoute);
        return ResponseEntity.ok("✅ Bus route successfully added!");
    }
    
    
//    {
//    	  "routeNumber": "R101",
//    	  "driverName": "John Doe",
//    	  "currentLocation": "Depot A",
//    	  "nextStop": "Stop 5",
//    	  "status": "Active",
//    	  "vehicle": {
//    	    "id": "c4ade368-b56f-4efe-a3ce-2441873c7126"
//    	  },
//    	  "distance": 25.0,
//    	  "startTime": "08:00:00",
//    	  "endTime": "10:00:00",
//    	  "etaSchool": "09:45"
//    	}

   
    /**
     * API: Add a new stop to a specific bus route
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/route-stops/{busRouteId}/stops
     * PATH PARAM: busRouteId
     * BODY: RouteStop JSON
     * RESPONSE: Saved RouteStop JSON
     */
    @PostMapping("/{busRouteId}/stops")
    public ResponseEntity<RouteStopDto> addStopToBusRoute(
            @PathVariable String busRouteId,
            @RequestBody RouteStopDto stopDto) {

        RouteStopDto savedStop = routeStopService.addStopToBusRoute(busRouteId, stopDto);
        return ResponseEntity.ok(savedStop);
    }



    // ---------------- PUT ----------------

    /**
     * API: Update bus route by ID
     * METHOD: PUT
     * URL: http://localhost:8080/api/non-teaching-staff/bus-routes/{id}
     * PURPOSE: Update details of an existing bus route
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBusRoute(@PathVariable String id, @RequestBody BusRoute busRoute) {
        busRouteService.updateBusRoute(id, busRoute);
        return ResponseEntity.ok("✅ Bus route successfully updated!");
    }

    // ---------------- DELETE ----------------

    /**
     * API: Delete bus route by ID
     * METHOD: DELETE
     * URL: http://localhost:8080/api/non-teaching-staff/bus-routes/{id}
     * PURPOSE: Permanently delete a bus route by its ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBusRoute(@PathVariable String id) {
        busRouteService.deleteBusRoute(id);
        return ResponseEntity.ok("🗑️ Bus route successfully deleted!");
    }

    // ---------------- Exception Handling ----------------

    @ExceptionHandler(DetailsNotFoundException.class)
    public ResponseEntity<String> handleDetailsNotFound(DetailsNotFoundException ex) {
        return ResponseEntity.status(404).body("❌ Not Found: " + ex.getMessage());
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<String> handleOperationFailed(OperationFailedException ex) {
        return ResponseEntity.status(500).body("⚠️ Operation Failed: " + ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body("❌ Error: " + ex.getMessage());
    }
}
