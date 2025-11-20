package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "type", nullable = false)
    private String type; // EMERGENCY, INFO, ISSUE, FUEL

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "receiver_role", nullable = false)
    private String receiverRole; // ADMIN, DRIVER, STAFF

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    // Reference to Driver
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "is_read")
    private Boolean isRead = false;
}
