package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "route_stops")
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "bus_route_id", nullable = false)
    @JsonBackReference
    private BusRoute busRoute;


    @Column(name = "stop_name")
    private String stopName;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Column(name = "student_count")
    private Integer studentCount;

    @Column(name = "status")
    private String status; // upcoming / completed / pending
    
    @Column(name = "distance_from_previous")
    private Double distanceFromPrevious;
    
    @Column(name = "stop_order")
    private Integer stopOrder; // example: 1, 2, 3...

    
    
    

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
