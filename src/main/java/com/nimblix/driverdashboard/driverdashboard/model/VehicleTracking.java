package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_tracking")
public class VehicleTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "vehicle_id", columnDefinition = "CHAR(36)", nullable = false)
    private String vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Vehicle vehicle;

    @Column(name = "driver_id", length = 36, nullable = false)
    private String driverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ToString.Exclude
    private Driver driver;
    
    @Column(name = "start_km_today")
    private Integer startKmToday;

    @Column(name = "end_km_today")
    private Integer endKmToday;

    public Integer getMileageToday() {
        if (startKmToday != null && endKmToday != null) {
            return endKmToday - startKmToday;
        }
        return 0;
    }
    

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "route_number")
    private String routeNumber;

    @Column(name = "current_km")
    private Integer currentKm = 0;

    @Column(name = "fuel_level")
    private Double  fuelLevel = 100.00;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "speed")
    private String speed;

    @Column(name = "engine_status")
    private String engineStatus = "OFF";

    @Column(name = "engine_temperature")
    private String engineTemperature;

    @Column(name = "gps_signal_strength")
    private String gpsSignalStrength;

    @Column(name = "is_moving")
    private Boolean isMoving;

    @Column(name = "last_maintenance_km")
    private Integer lastMaintenanceKm;

    @Column(name = "next_maintenance_due")
    private Integer nextMaintenanceDue;

    @Column(name = "emergency_alert", length = 10)
    private String emergencyAlert;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "model")
    private String model;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "insurance")
    private LocalDate insuranceValidUntil;

    // Relations for logs
    @OneToMany(mappedBy = "vehicleTracking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<FuelLog> fuelLogs;

    @OneToMany(mappedBy = "vehicleTracking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<MaintenanceItemEntity> maintenanceItems;

    @OneToMany(mappedBy = "vehicleTracking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<SafetyChecklist> safetyChecklists;
}
