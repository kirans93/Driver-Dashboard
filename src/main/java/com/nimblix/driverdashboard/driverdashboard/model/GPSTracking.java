package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gps_tracking")
public class GPSTracking {

	@Id 
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", columnDefinition = "CHAR(36)")

	private String id;
    private String latitude;
	private String longitude;
	private String speed;
	private String direction;

	@Column(name = "timestamp")
	private LocalDateTime timestamp;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
	private Vehicle vehicle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", referencedColumnName = "id")
	private Driver driver;

	private String status = "active";

	// 🔹 New field
	@Column(name = "source")
	private String source; // DEVICE / VEHICLE_SENSOR


}
