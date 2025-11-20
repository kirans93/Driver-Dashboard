package com.nimblix.driverdashboard.driverdashboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.RouteStop;
import com.nimblix.driverdashboard.driverdashboard.model.Trip;
import com.nimblix.driverdashboard.driverdashboard.repository.BusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.RouteStopRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.TripRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final BusRouteRepository busRouteRepository;
	private final RouteStopRepository  routeStopRepository;

  
    public TripService(TripRepository tripRepository, BusRouteRepository busRouteRepository,
			RouteStopRepository routeStopStatusRepository) {
		
		this.tripRepository = tripRepository;
		this.busRouteRepository = busRouteRepository;
		this.routeStopRepository = routeStopStatusRepository;
	}

	// ------------------------
    // Start Trip
    // ------------------------
    @Transactional
    public Trip startTrip(String driverId, String vehicleId, String routeNumber, LocalTime scheduledTime) {

        LocalDate today = LocalDate.now();

        // Check if trip already exists today
        Trip existing = tripRepository.findByDriverIdAndTripDate(driverId, today)
                .stream()
                .filter(t -> t.getRouteNumber().equals(routeNumber))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            // Trip exists but not started
            if (existing.getStatus() == Trip.TripStatus.ASSIGNED) {
                existing.setStatus(Trip.TripStatus.STARTED);
                existing.setStartTime(LocalTime.now());
                return tripRepository.save(existing);
            }
            return existing; // Already started or completed
        }

        // Fetch bus route
        BusRoute busRoute = busRouteRepository.findByRouteNumber(routeNumber);
        if (busRoute == null) throw new RuntimeException("Bus route not found");

        int studentsCount = busRoute.getStudents() != null ? busRoute.getStudents().size() : 0;
        double totalDistance = busRoute.getDistance() != null ? busRoute.getDistance() : 0.0;

        // Get first stop
        List<RouteStop> stops = busRoute.getStops();
        RouteStop firstStop = null;
        if (stops != null && !stops.isEmpty()) {
            firstStop = stops.stream()
                    .sorted(Comparator.comparingInt(RouteStop::getStopOrder))
                    .findFirst()
                    .orElse(null);
        }

        // Create new trip
        Trip trip = new Trip();
        trip.setId(UUID.randomUUID().toString());
        trip.setDriverId(driverId);
        trip.setVehicleId(vehicleId);
        trip.setTripDate(today);
        trip.setScheduledTime(scheduledTime);
        trip.setStatus(Trip.TripStatus.STARTED);
        trip.setRouteNumber(routeNumber);
        trip.setStudentsCount(studentsCount);
        trip.setTotalDistance(totalDistance);
        trip.setDistanceCovered(0.0);
        trip.setCurrentStop(firstStop != null ? firstStop.getStopName() : "Not started");
        trip.setStartTime(LocalTime.now());

        return tripRepository.save(trip);
    }

    // ------------------------
    // Mark Stop
    // ------------------------
    @Transactional
    public Trip markStop(String tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != Trip.TripStatus.STARTED) {
            throw new RuntimeException("Trip is not started");
        }

        // Fetch route
        BusRoute busRoute = busRouteRepository.findByRouteNumber(trip.getRouteNumber());
        if (busRoute == null) {
            throw new RuntimeException("Bus route not found");
        }

        List<RouteStop> stops = busRoute.getStops();
        if (stops == null || stops.isEmpty()) {
            return trip;
        }

        // Sort stops in correct order
        stops.sort(Comparator.comparingInt(RouteStop::getStopOrder));

        // Find index of current stop
        int currentIndex = -1;
        if (trip.getCurrentStop() != null) {
            currentIndex = stops.stream()
                    .map(RouteStop::getStopName)
                    .toList()
                    .indexOf(trip.getCurrentStop());
        }

        // Determine next stop
        RouteStop nextStop = (currentIndex + 1 < stops.size()) ? stops.get(currentIndex + 1) : null;

        if (nextStop != null) {
            // Update trip with next stop name
            trip.setCurrentStop(nextStop.getStopName());

            // Update distance covered
            double distancePerStop = busRoute.getDistance() / stops.size();
            trip.setDistanceCovered(Math.min(
                    trip.getDistanceCovered() + distancePerStop,
                    busRoute.getDistance()
            ));

        } else {
            // No next stop — trip completed
            trip.setStatus(Trip.TripStatus.COMPLETED);
            trip.setCurrentStop(null); // Optional reset
            trip.setDistanceCovered(busRoute.getDistance());

            // Reset stop statuses to UPCOMING for next trip
            routeStopRepository.resetAllStatusesToUpcoming(tripId);
        }

        return tripRepository.save(trip);
    }

    // ------------------------
    // Complete Trip
    // ------------------------
    @Transactional
    public Trip completeTrip(String tripId, LocalDateTime arrivalTime) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (trip.getStatus() != Trip.TripStatus.STARTED) {
            throw new RuntimeException("Trip is not started or already completed");
        }

        trip.setEndTime(LocalTime.now());
        trip.setArrivalTime(arrivalTime);
        trip.setStatus(Trip.TripStatus.COMPLETED);
        trip.setDistanceCovered(trip.getTotalDistance());
        trip.setCurrentStop("Completed");

        tripRepository.save(trip);

        // Reset stops
        if (trip.getRouteNumber() != null) {
            BusRoute busRoute = busRouteRepository.findByRouteNumber(trip.getRouteNumber());
            if (busRoute != null) {
                routeStopRepository.resetAllStatusesToUpcoming(busRoute.getId());
            }
        }

        return trip;
    }




}
