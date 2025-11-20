package com.nimblix.driverdashboard.driverdashboard.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.RouteStop;
import com.nimblix.driverdashboard.driverdashboard.model.Vehicle;
import com.nimblix.driverdashboard.driverdashboard.repository.BusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleRepository;
import com.nimblix.driverdashboard.driverdashboard.dto.RouteStopDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.exception.OperationFailedException;

@Service
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final VehicleRepository vehicleRepository;

    public BusRouteService(BusRouteRepository busRouteRepository,
                           VehicleRepository vehicleRepository) {
        this.busRouteRepository = busRouteRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // ---------------- GET ----------------
    public BusRoute getBusRouteById(String id) {
        return busRouteRepository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("BusRoute not found with id: " + id));
    }

    public List<BusRoute> getAllBusRoutes() {
        return busRouteRepository.findAll();
    }

    // ---------------- POST ----------------
    public BusRoute saveBusRoute(BusRoute busRoute) {
        try {
            // Validate Vehicle
            if (busRoute.getVehicle() == null || busRoute.getVehicle().getId() == null) {
                throw new OperationFailedException("Vehicle must be provided for BusRoute");
            }

            Vehicle vehicle = vehicleRepository.findById(busRoute.getVehicle().getId())
                    .orElseThrow(() -> new DetailsNotFoundException(
                            "Vehicle not found with id: " + busRoute.getVehicle().getId()));

            busRoute.setVehicle(vehicle); // set managed entity
            busRoute.setEta(UUID.randomUUID().toString());
            return busRouteRepository.save(busRoute);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to save BusRoute: " + e.getMessage());
        }
    }

    // ---------------- PUT ----------------
    public BusRoute updateBusRoute(String id, BusRoute updatedRoute) {
        BusRoute existing = busRouteRepository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("BusRoute not found with id: " + id));

        try {
            existing.setRouteNumber(updatedRoute.getRouteNumber());
            existing.setDriverName(updatedRoute.getDriverName());
            existing.setCurrentLocation(updatedRoute.getCurrentLocation());
            existing.setNextStop(updatedRoute.getNextStop());
            existing.setEta(updatedRoute.getEta());
            existing.setStatus(updatedRoute.getStatus());
            existing.setStops(updatedRoute.getStops());

            // Validate and update vehicle if provided
            if (updatedRoute.getVehicle() != null && updatedRoute.getVehicle().getId() != null) {
                Vehicle vehicle = vehicleRepository.findById(updatedRoute.getVehicle().getId())
                        .orElseThrow(() -> new DetailsNotFoundException(
                                "Vehicle not found with id: " + updatedRoute.getVehicle().getId()));
                existing.setVehicle(vehicle);
            }

            return busRouteRepository.save(existing);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update BusRoute: " + e.getMessage());
        }
    }

    // ---------------- DELETE ----------------
    public void deleteBusRoute(String id) {
        if (!busRouteRepository.existsById(id)) {
            throw new DetailsNotFoundException("BusRoute not found with id: " + id);
        }
        try {
            busRouteRepository.deleteById(id);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete BusRoute with id " + id);
        }
    }

    // ---------------- ADD STOP ----------------
    public RouteStopDto addStopToBusRoute(String busRouteId, RouteStopDto stopDto) {
        BusRoute busRoute = busRouteRepository.findById(busRouteId)
                .orElseThrow(() -> new DetailsNotFoundException("Bus route not found with id: " + busRouteId));

        RouteStop stop = new RouteStop();
        stop.setStopName(stopDto.getStopName());
        stop.setScheduledTime(stopDto.getScheduledTime());
        stop.setStudentCount(stopDto.getStudentCount());
        stop.setStatus(stopDto.getStatus());
        stop.setBusRoute(busRoute);

        busRoute.getStops().add(stop);
        busRouteRepository.save(busRoute);

        stopDto.setId(stop.getId());
        stopDto.setBusRouteId(busRoute.getId());
        return stopDto;
    }
}
