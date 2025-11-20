package com.nimblix.driverdashboard.driverdashboard.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.RouteStop;
import com.nimblix.driverdashboard.driverdashboard.model.Trip;
import com.nimblix.driverdashboard.driverdashboard.repository.BusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.TripRepository;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BusRouteRepository busRouteRepository;

    /** Start a new trip */
    @PostMapping("/start")
    public Trip startTrip(@RequestParam String driverId,
                          @RequestParam String vehicleId,
                          @RequestParam String routeNumber,
                          @RequestParam int studentsCount) {

        BusRoute busRoute = busRouteRepository.findByRouteNumber(routeNumber);
        if (busRoute == null) throw new RuntimeException("Bus route not found");

        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());
        trip.setDriverId(driverId);
        trip.setVehicleId(vehicleId);
        trip.setTripDate(LocalDate.now());
        trip.setStartTime(LocalTime.now());
        trip.setScheduledTime(busRoute.getScheduledTime()); // get from BusRoute
        trip.setStatus(Trip.TripStatus.STARTED);
        trip.setRouteNumber(routeNumber);
        trip.setStudentsCount(studentsCount);
        trip.setTotalDistance(busRoute.getDistance());
        trip.setDistanceCovered(0.0);

        return tripRepository.save(trip);
    }


    /** Complete the trip */
    @PostMapping("/complete/{tripId}")
    public Trip completeTrip(@PathVariable String tripId,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime arrivalTime) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setEndTime(LocalTime.now());
        trip.setArrivalTime(arrivalTime);
        trip.setStatus(Trip.TripStatus.COMPLETED);
        trip.setDistanceCovered(trip.getTotalDistance()); // final distance

        return tripRepository.save(trip);
    }

    /** Mark a stop reached */
    @PostMapping("/mark-stop/{tripId}/{stopId}")
    public Trip markStop(@PathVariable String tripId,
                         @PathVariable String stopId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        BusRoute busRoute = busRouteRepository.findByRouteNumber(trip.getRouteNumber());
        RouteStop stop = busRoute.getStops().stream()
                .filter(s -> s.getId().equals(stopId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Stop not found"));

        // Mark stop as completed
        stop.setStatus("COMPLETED");

        // Add distance from previous stop to trip
        double distanceFromPrev = stop.getDistanceFromPrevious() != null ? stop.getDistanceFromPrevious() : 0.0;
        trip.setDistanceCovered(trip.getDistanceCovered() + distanceFromPrev);

        // Optionally, set current stop name
        trip.setCurrentStop(stop.getStopName());

        return tripRepository.save(trip);
    }



    /** Get today's trips for driver */
    @GetMapping("/today/{driverId}")
    public List<Trip> getTodaysTrips(@PathVariable String driverId) {
        return tripRepository.findByDriverIdAndTripDate(driverId, LocalDate.now());
    }
}
