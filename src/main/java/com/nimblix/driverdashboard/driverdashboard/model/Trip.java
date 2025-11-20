package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;
    
    
    
    @Column(name = "total_distance")
    private Double totalDistance; // total distance of the route

    @Column(name = "distance_covered")
    private Double distanceCovered; // distance already covered

    @Column(name = "current_stop")
    private String currentStop;
    
    


    @Column(name = "driver_id", columnDefinition = "CHAR(36)", nullable = false)
    private String driverId;

    @Column(name = "vehicle_id", columnDefinition = "CHAR(36)", nullable = false)
    private String vehicleId;

    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "scheduled_time", nullable = false)
    private LocalTime scheduledTime; // route start time, e.g., 09:30

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TripStatus status = TripStatus.ASSIGNED;

    @Column(name = "students_count", nullable = false)
    private Integer studentsCount = 0;

    @Column(name = "route_number")
    private String routeNumber;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public enum TripStatus {
        ASSIGNED,
        STARTED,
        COMPLETED,
        CANCELLED
    }

}
