package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;

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
@Table(name = "emergency_alerts")
public class EmergencyAlert {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	// Reference to Vehicle
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    // Reference to Driver
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
    private Driver driver;

    private LocalDateTime alertTime;

    private Double latitude;

    private Double longitude;

    private String alertType; // e.g., accident, breakdown, medical

    private String status; // active / resolved

    private LocalDateTime notifiedAt;
}
