package com.nimblix.driverdashboard.driverdashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimblix.driverdashboard.driverdashboard.dto.RouteStopDto;
import com.nimblix.driverdashboard.driverdashboard.service.RouteStopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/route-stops")
@RequiredArgsConstructor
public class RouteStopController {

    private final RouteStopService routeStopService;

    // ========================= GET =========================
    /**
     * API: Fetch all stops for a bus route
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/route-stops/bus-route/{busRouteId}
     * PATH PARAM: busRouteId
     * RESPONSE: List of RouteStopDto JSON
     */
    @GetMapping("/bus-route/{busRouteId}")
    public ResponseEntity<List<RouteStopDto>> getStops(@PathVariable String busRouteId) {
        List<RouteStopDto> stops = routeStopService.getStopsByBusRoute(busRouteId);
        return ResponseEntity.ok(stops);
    }

    // ========================= POST =========================
    /**
     * API: Add a new stop to a bus route
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/route-stops/bus-route/{busRouteId}
     * PATH PARAM: busRouteId
     * BODY: RouteStopDto JSON
     * RESPONSE: Saved RouteStopDto JSON
     */
    @PostMapping("/bus-route/{busRouteId}")
    public ResponseEntity<RouteStopDto> addStop(
            @PathVariable String busRouteId,
            @RequestBody RouteStopDto routeStopDto
    ) {
        RouteStopDto savedStop = routeStopService.addRouteStop(busRouteId, routeStopDto);
        return ResponseEntity.ok(savedStop);
    }
}
