package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student_transportation")
public class StudentTransportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String driverId;

    @Column(nullable = false)
    private String routeId;

    @Column(nullable = false)
    private LocalDate transportDate;

    @Column(nullable = false)
    private boolean transported = true;

    private LocalDateTime pickupTime;
    
    private LocalDateTime dropoffTime;

    @Column(name = "picked_up", nullable = false)
    private Boolean pickedUp = false;

    @Column(name = "dropped_off", nullable = false)
    private Boolean droppedOff = false;

    @Column(name = "on_time", nullable = false)
    private Boolean onTime = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (pickedUp == null) pickedUp = false;
        if (droppedOff == null) droppedOff = false;
        if (onTime == null) onTime = true;
    }
}
