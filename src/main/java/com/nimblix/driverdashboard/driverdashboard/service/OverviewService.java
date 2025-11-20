package com.nimblix.driverdashboard.driverdashboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.DriverInfoDto;
import com.nimblix.driverdashboard.driverdashboard.dto.OverviewResponse;
import com.nimblix.driverdashboard.driverdashboard.dto.PerformanceInfoDto;
import com.nimblix.driverdashboard.driverdashboard.dto.VehicleInfoDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.exception.OperationFailedException;
import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.Trip;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.BusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.DriverRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.TripRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleTrackingRepository;


@Service
public class OverviewService {

    private final DriverRepository driverRepository;
    private final VehicleTrackingRepository vehicleTrackingRepository;
    private final BusRouteRepository busRouteRepository;
	private final TripRepository tripRepository;
	private final TripService tripService;

  
 public OverviewService(DriverRepository driverRepository, VehicleTrackingRepository vehicleTrackingRepository,
			BusRouteRepository busRouteRepository, TripRepository tripRepository , TripService tripService) {
		
	 
		this.driverRepository = driverRepository;
		this.vehicleTrackingRepository = vehicleTrackingRepository;
		this.busRouteRepository = busRouteRepository;
		this.tripRepository = tripRepository;
		this.tripService = tripService;
	}
    // ---------------- GET ----------------

   

	public OverviewResponse getDriverOverview(String driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found with id: " + driverId));
        return buildOverviewResponse(driver);
    }

    public OverviewResponse getDriverOverviewByEmployeeId(String employeeId) {
        Driver driver = driverRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found with employeeId: " + employeeId));
        return buildOverviewResponse(driver);
    }

    public OverviewResponse getDriverOverviewByVehicle(String vehicleNumber) {
        Driver driver = driverRepository.findByVehicleAssigned(vehicleNumber)
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found with vehicle: " + vehicleNumber));
        return buildOverviewResponse(driver);
    }

    // ---------------- POST ----------------

    public VehicleTracking saveVehicleTracking(VehicleTracking vehicleTracking) {
        try {
            if (vehicleTracking.getId() == null) {
                vehicleTracking.setId(UUID.randomUUID().toString());
            }
            return vehicleTrackingRepository.save(vehicleTracking);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to save VehicleTracking: " + e.getMessage());
        }
    }

    public Driver saveDriver(Driver driver) {
        if (driver.getId() == null) {
            driver.setId(UUID.randomUUID().toString());
        }

        if (driver.getUser() != null && driver.getUser().getId() == null) {
            driver.getUser().setId(UUID.randomUUID().toString());
            driver.getUser().setCreatedAt(LocalDateTime.now());
            driver.getUser().setUpdatedAt(LocalDateTime.now());
        }

        return driverRepository.save(driver);
    }

    // ---------------- PUT ----------------

    public VehicleTracking updateVehicleTracking(String id, VehicleTracking updatedTracking) {
        VehicleTracking existing = vehicleTrackingRepository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("VehicleTracking not found with id: " + id));

        try {
            existing.setFuelLevel(updatedTracking.getFuelLevel());
            existing.setEngineStatus(updatedTracking.getEngineStatus());
            existing.setCurrentKm(updatedTracking.getCurrentKm());
            existing.setNextMaintenanceDue(updatedTracking.getNextMaintenanceDue());

            return vehicleTrackingRepository.save(existing);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update VehicleTracking: " + e.getMessage());
        }
    }

    public Driver updateDriver(String id, Driver updatedDriver) {
        Driver existing = driverRepository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found with id: " + id));

        try {
            existing.setEmployeeId(updatedDriver.getEmployeeId());
            existing.setDepartment(updatedDriver.getDepartment());
            existing.setShift(updatedDriver.getShift());
            existing.setVehicleAssigned(updatedDriver.getVehicleAssigned());
            existing.setPhone(updatedDriver.getPhone());

            return driverRepository.save(existing);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to update Driver: " + e.getMessage());
        }
    }

    // ---------------- DELETE ----------------

    public void deleteVehicleTracking(String id) {
        if (!vehicleTrackingRepository.existsById(id)) {
            throw new DetailsNotFoundException("VehicleTracking not found with id: " + id);
        }
        try {
            vehicleTrackingRepository.deleteById(id);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete VehicleTracking with id " + id);
        }
    }

    public void deleteDriver(String id) {
        if (!driverRepository.existsById(id)) {
            throw new DetailsNotFoundException("Driver not found with id: " + id);
        }
        try {
            driverRepository.deleteById(id);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to delete Driver with id " + id);
        }
    }

    // ---------------- PRIVATE HELPERS ----------------

    private OverviewResponse buildOverviewResponse(Driver driver) {
        OverviewResponse response = new OverviewResponse();

        // --- Driver Info ---
        DriverInfoDto driverInfo = new DriverInfoDto();
        driverInfo.setDepartment(driver.getDepartment());
        driverInfo.setDriverId(driver.getId());
        driverInfo.setShift(driver.getShift());
        driverInfo.setVehicleAssigned(driver.getVehicleAssigned());
        driverInfo.setPhone(driver.getPhone());
        response.setDriverInfo(driverInfo);

        // --- Vehicle Info ---
        VehicleTracking vehicleTracking = vehicleTrackingRepository
                .findTopByDriverIdOrderByTimestampDesc(driver.getId())
                .orElse(null);

        if (vehicleTracking != null) {
            VehicleInfoDto vehicleInfo = new VehicleInfoDto();
            vehicleInfo.setEngineHealth(vehicleTracking.getEngineStatus());
            vehicleInfo.setFuelLevel(vehicleTracking.getFuelLevel() + "%");
            vehicleInfo.setNextService("Due at " + vehicleTracking.getNextMaintenanceDue() + " km");

            Integer startKm = vehicleTracking.getStartKmToday();
            Integer endKm = vehicleTracking.getEndKmToday();

            if (startKm != null && endKm != null) {
                vehicleInfo.setMileageToday((endKm - startKm) + " km");
            } else {
                vehicleInfo.setMileageToday("0 km");
            }

            response.setVehicleInfo(vehicleInfo);

            
            
            // --- Auto-start trip here ---
            String vehicleId = driver.getVehicleAssigned();
            BusRoute busRoute = busRouteRepository.findByVehicleNumber(vehicleId);
            if (busRoute != null) {
                String routeNumber = busRoute.getRouteNumber();
                LocalTime scheduledTime = busRoute.getScheduledTime(); // from DB
                tripService.startTrip(driver.getId(), vehicleId, routeNumber, scheduledTime);

            }
        }

// extra
        
//     // --- Auto-complete trip if engine is OFF ---
//        if (vehicleTracking != null && "OFF".equalsIgnoreCase(vehicleTracking.getEngineStatus())) {
//            BusRoute busRoute = busRouteRepository.findByVehicleNumber(driver.getVehicleAssigned());
//            if (busRoute != null) {
//                String routeNumber = busRoute.getRouteNumber();
//                tripService.autoCompleteTrip(driver.getId(), routeNumber);
//            }
//        }

        
        
        if (vehicleTracking != null && "OFF".equalsIgnoreCase(vehicleTracking.getEngineStatus())) {
            BusRoute busRoute = busRouteRepository.findByVehicleNumber(driver.getVehicleAssigned());
            if (busRoute != null) {
                String routeNumber = busRoute.getRouteNumber();
                // find today's trip
                Trip todayTrip = tripRepository.findByDriverIdAndTripDate(driver.getId(), LocalDate.now())
                                               .stream()
                                               .filter(t -> t.getRouteNumber().equals(routeNumber)
                                                       && t.getStatus() == Trip.TripStatus.STARTED)
                                               .findFirst()
                                               .orElse(null);
                if (todayTrip != null) {
                    tripService.completeTrip(todayTrip.getId(), LocalDateTime.now());
                }
            }
        }

        
        // --- Performance Info ---
        LocalDate today = LocalDate.now();
        List<Trip> todayTrips = tripRepository.findByDriverIdAndTripDate(driver.getId(), today);

        int totalTrips = todayTrips.size();
        int completedTrips = (int) todayTrips.stream()
                .filter(t -> t.getStatus() == Trip.TripStatus.COMPLETED)
                .count();

        double totalScore = 0;
        for (Trip trip : todayTrips) {
            if (trip.getStatus() != Trip.TripStatus.COMPLETED || trip.getArrivalTime() == null) continue;

            LocalTime scheduledTime = trip.getScheduledTime();
            LocalDateTime arrivalTime = trip.getArrivalTime();
            long delayMinutes = java.time.Duration.between(scheduledTime, arrivalTime).toMinutes();

            double tripScore;
            if (delayMinutes <= 0) tripScore = 100;
            else if (delayMinutes <= 10) tripScore = 80;
            else if (delayMinutes <= 20) tripScore = 50;
            else tripScore = 0;

            totalScore += tripScore;
        }

        double onTimePerformance = totalTrips > 0 ? totalScore / totalTrips : 0;

        PerformanceInfoDto performance = new PerformanceInfoDto();
        performance.setAttendance("Present"); // hardcoded
        performance.setTripsCompleted(completedTrips + "/" + totalTrips);
        performance.setOnTimePerformance(String.format("%.1f%%", onTimePerformance));

        response.setPerformanceInfo(performance);

        return response;
    }


}
