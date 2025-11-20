package com.nimblix.driverdashboard.driverdashboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.AddVehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.EmergencyAlertDto;
import com.nimblix.driverdashboard.driverdashboard.dto.FuelEntryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MaintenanceLogDto;
import com.nimblix.driverdashboard.driverdashboard.dto.MyVehicleDashboardDto;
import com.nimblix.driverdashboard.driverdashboard.dto.RouteSummaryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.StartRouteDto;
import com.nimblix.driverdashboard.driverdashboard.dto.UpdateLocationDto;
import com.nimblix.driverdashboard.driverdashboard.dto.UpdateVehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.VehicleDto;
import com.nimblix.driverdashboard.driverdashboard.dto.VehicleStatusDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.EmergencyAlert;
import com.nimblix.driverdashboard.driverdashboard.model.FuelEntry;
import com.nimblix.driverdashboard.driverdashboard.model.GPSTracking;
import com.nimblix.driverdashboard.driverdashboard.model.MaintenanceLog;
import com.nimblix.driverdashboard.driverdashboard.model.Notification;
import com.nimblix.driverdashboard.driverdashboard.model.Vehicle;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.FuelEntryRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.GPSTrackingRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.MyVehicalMaintenanceItemRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.MyVehicleBusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.MyVehicleTrackingRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.NotificationRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.StudentRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleRepository;

import jakarta.transaction.Transactional;

import com.nimblix.driverdashboard.driverdashboard.repository.DriverRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.EmergencyAlertRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyVehicleService {

    private final VehicleRepository vehicleRepository;
    private final MyVehicleTrackingRepository vehicleTrackingRepository;
    private final FuelEntryRepository fuelEntryRepository;
    private final MyVehicalMaintenanceItemRepository maintenanceRepository;
    private final NotificationRepository notificationRepository;
    private final MyVehicleBusRouteRepository  busRouteRepository;
    private final StudentRepository studentRepository;
    private final DriverRepository driverRepository;
    private final EmergencyAlertRepository emergencyAlertRepository;
	private final GPSTrackingRepository gpsTrackingRepository;
    
    // ------------------Add and Update the Vehical ------------------
    
    @Transactional
    public Vehicle addVehicle(AddVehicleDto dto) {
        if (vehicleRepository.existsByVehicleNumber(dto.getVehicleNumber())) {
            throw new RuntimeException("Vehicle already exists with number: " + dto.getVehicleNumber());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setModel(dto.getModel());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setInsuranceValidity(dto.getInsuranceValidity());

        if (dto.getAssignedDriverId() != null) {
            Driver driver = driverRepository.findById(dto.getAssignedDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            vehicle.setAssignedDriver(driver);
        }

        return vehicleRepository.save(vehicle);
    }
    
    
    @Transactional
    public Vehicle updateVehicle(String vehicleId, UpdateVehicleDto dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        if (dto.getModel() != null) {
            vehicle.setModel(dto.getModel());
        }
        if (dto.getCapacity() != null) {
            vehicle.setCapacity(dto.getCapacity());
        }
        if (dto.getInsuranceValidity() != null) {
            vehicle.setInsuranceValidity(dto.getInsuranceValidity());
        }
        if (dto.getAssignedDriverId() != null) {
            Driver driver = driverRepository.findById(dto.getAssignedDriverId())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
            vehicle.setAssignedDriver(driver);
        }

        return vehicleRepository.save(vehicle);
    }

    
    
    // ---------------- Vehicle Details ----------------
    public VehicleDto getVehicleDetails(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new DetailsNotFoundException("Vehicle not found"));
        return mapVehicleToDto(vehicle);
    }


    public VehicleStatusDto getLiveStatus(String vehicleId) {
        VehicleTracking tracking = vehicleTrackingRepository.findTopByVehicleIdOrderByTimestampDesc(vehicleId.toString())
                .orElseThrow(() -> new DetailsNotFoundException("No tracking data available"));

        Double speed = 0.0;
        try { speed = Double.parseDouble(tracking.getSpeed()); } catch (Exception ignored) {}

        Double fuelLevel = tracking.getFuelLevel() != null ? tracking.getFuelLevel().doubleValue() : 0.0;
        Boolean isMoving = tracking.getIsMoving() != null && tracking.getIsMoving();

        return new VehicleStatusDto(
                tracking.getEngineTemperature(),
                speed,
                tracking.getGpsSignalStrength(),
                fuelLevel,
                isMoving
        );
    }

    // ---------------- Today's Route ----------------
    public RouteSummaryDto getTodaysRoute(String vehicleId) {
        // Fetch today's route for the vehicle
        BusRoute route = busRouteRepository
                .findTopByVehicle_IdAndDate(vehicleId, LocalDate.now())
                .orElseThrow(() -> new DetailsNotFoundException("No route assigned for today"));

        // Count students assigned to this route
        long studentsOnboard = studentRepository.countByBusRouteId(route.getId());

        return new RouteSummaryDto(
                route.getRouteNumber(),           // routeNumber
                (int) studentsOnboard,            // students onboard
                route.getStudents() != null ? route.getStudents().size() : 0,  // capacity from students list if exists
                route.getDistance(),              // distance
                route.getEtaSchool()              // ETA
        );
    }



    // ---------------- MyVehicle Dashboard ----------------
    public MyVehicleDashboardDto getDashboard(String vehicleId) {
        VehicleDto vehicleDetails = getVehicleDetails(vehicleId); // VehicleDto mapping already uses String
        VehicleStatusDto liveStatus = getLiveStatus(vehicleId);
        RouteSummaryDto todayRoute = getTodaysRoute(vehicleId);

        return new MyVehicleDashboardDto(vehicleDetails, liveStatus, todayRoute);
    }



    // ---------------- Start Route ----------------
    public String startRoute(StartRouteDto dto) {
        // fetch route
        BusRoute route = busRouteRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new DetailsNotFoundException("Route not found"));

        // fetch vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new DetailsNotFoundException("Vehicle not found"));

        // fetch driver
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

        // create GPS tracking record
        GPSTracking gpsTracking = new GPSTracking();
        gpsTracking.setId(UUID.randomUUID().toString());
        gpsTracking.setVehicle(vehicle);
        gpsTracking.setDriver(driver);
        gpsTracking.setLatitude(dto.getLatitude());
        gpsTracking.setLongitude(dto.getLongitude());
        gpsTracking.setSpeed("0");        // default speed at start
        gpsTracking.setDirection("0");    // default direction
        gpsTracking.setTimestamp(LocalDateTime.now());
        gpsTracking.setStatus("active");
        gpsTracking.setSource("DEVICE");  // can be VEHICLE_SENSOR if applicable

        gpsTrackingRepository.save(gpsTracking);

        return "Route started: " + route.getRouteNumber();
    }


    
    // ---------------- Update Vehicle Location ----------------
    public VehicleTracking updateLocation(UpdateLocationDto dto) {
        // Fetch the latest tracking entry for this vehicle
    	VehicleTracking tracking = vehicleTrackingRepository.findById(dto.getVehicleId())
    	        .orElseThrow(() -> new DetailsNotFoundException("Vehicle not found"));

    	if (dto.getEngineTemperature() != null) {
    	    tracking.setEngineTemperature(String.valueOf(dto.getEngineTemperature()));
    	}
    	if (dto.getFuelLevel() != null) {
    	    tracking.setFuelLevel(dto.getFuelLevel());  // no intValue()
    	}


    	tracking.setLatitude(dto.getLatitude());
    	tracking.setLongitude(dto.getLongitude());
    	tracking.setSpeed(dto.getSpeed() != null ? String.valueOf(dto.getSpeed()) : "0");
    	tracking.setIsMoving(dto.getIsMoving() != null ? dto.getIsMoving() : false);
    	tracking.setTimestamp(LocalDateTime.now());

    	return vehicleTrackingRepository.save(tracking);

    }




    // ---------------- Fuel Entry ----------------
    public FuelEntryDto recordFuelEntry(FuelEntryDto dto) {
        // 1. Fetch Vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new DetailsNotFoundException("Vehicle not found"));

        // 2. Fetch Driver
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

        // 3. Map DTO -> Entity
        FuelEntry fuelEntry = new FuelEntry();
        fuelEntry.setId(dto.getId()); // UUID will auto-generate if null
        fuelEntry.setVehicle(vehicle);
        fuelEntry.setDriver(driver);
        fuelEntry.setFuelAddedLitres(dto.getFuelAddedLitres());
        fuelEntry.setFuelCost(dto.getFuelCost() != null ? dto.getFuelCost() : 0);
        fuelEntry.setOdometerKm(dto.getOdometerKm() != null ? dto.getOdometerKm() : 0);
        fuelEntry.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());

        // 4. Save Entity
        FuelEntry savedEntry = fuelEntryRepository.save(fuelEntry);

        // 5. Map back to DTO (include generated IDs)
        return new FuelEntryDto(
                savedEntry.getId(),
                savedEntry.getVehicle().getId(),
                savedEntry.getDriver().getId(),
                savedEntry.getFuelAddedLitres(),
                savedEntry.getFuelCost(),
                savedEntry.getOdometerKm(),
                savedEntry.getCreatedAt()
        );
    }



    // ---------------- Maintenance Log ----------------
    public MaintenanceLogDto logIssue(MaintenanceLogDto dto) {
        // 1️⃣ Fetch Vehicle
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new DetailsNotFoundException("Vehicle not found"));

        // 2️⃣ Fetch Driver
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

        // 3️⃣ Map DTO -> Entity
        MaintenanceLog item = new MaintenanceLog();
        item.setVehicle(vehicle);
        item.setDriver(driver);
        item.setIssueDescription(dto.getIssueDescription());
        item.setReportedAt(LocalDateTime.now());
        item.setStatus("OPEN");
        item.setResolvedBy(null); // Not resolved yet

        // 4️⃣ Save entity
        MaintenanceLog savedItem = maintenanceRepository.save(item);

        // 5️⃣ Map back to DTO
        return new MaintenanceLogDto(
                savedItem.getId(),
                savedItem.getVehicle().getId(),
                savedItem.getDriver().getId(),
                savedItem.getIssueDescription(),
                savedItem.getReportedAt(),
                savedItem.getStatus(),
                savedItem.getResolvedAt(),
                savedItem.getResolvedBy() != null ? savedItem.getResolvedBy().getId() : null,
                savedItem.getAttachments()
        );
    }



    // ---------------- Emergency Alert ----------------
    public EmergencyAlertDto sendEmergency(EmergencyAlertDto dto) {
        // 1️⃣ Fetch Vehicle and Driver entities
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + dto.getVehicleId()));

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + dto.getDriverId()));

        // 2️⃣ Map DTO -> EmergencyAlert entity
        EmergencyAlert alert = new EmergencyAlert();
        alert.setVehicle(vehicle);         // FK reference
        alert.setDriver(driver);           // FK reference
        alert.setAlertTime(LocalDateTime.now());
        alert.setLatitude(dto.getLatitude());
        alert.setLongitude(dto.getLongitude());
        alert.setAlertType(dto.getAlertType());
        alert.setStatus("SENT");

        // 3️⃣ Save Emergency Alert
        EmergencyAlert savedAlert = emergencyAlertRepository.save(alert);

        // 4️⃣ Map DTO -> Notification entity
        Notification notification = new Notification();
        notification.setVehicle(vehicle);   // Use entity reference
        notification.setDriver(driver);     // Use entity reference
        notification.setMessage(dto.getAlertType());
        notification.setTimestamp(LocalDateTime.now());
        notification.setType("EMERGENCY");
        notification.setReceiverRole("ADMIN");
        notificationRepository.save(notification);

        // 5️⃣ Map saved alert back to DTO
        return new EmergencyAlertDto(
                savedAlert.getId(),
                savedAlert.getVehicle().getId(),
                savedAlert.getDriver().getId(),
                savedAlert.getAlertTime(),
                savedAlert.getLatitude(),
                savedAlert.getLongitude(),
                savedAlert.getAlertType(),
                savedAlert.getStatus()
        );
    }


    // ---------------- Mappings ----------------
    private VehicleDto mapVehicleToDto(Vehicle vehicle) {
        String assignedDriverId = null;
        if (vehicle.getAssignedDriver() != null) {
            assignedDriverId = vehicle.getAssignedDriver().getId();
        }

        return new VehicleDto(
                vehicle.getId(),                  // String now
                vehicle.getVehicleNumber(),
                vehicle.getModel(),
                vehicle.getCapacity(),
                vehicle.getInsuranceValidity(),
                assignedDriverId                  // safe now
        );
    }

}
