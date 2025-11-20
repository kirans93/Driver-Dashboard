package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "maintenance_logs")
public class MaintenanceLog {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
	private Vehicle vehicle;

	// Reference to Driver
	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
	private Driver driver;

	@Column(nullable = false, length = 1000)
	private String issueDescription;

	private LocalDateTime reportedAt;

	@Column(nullable = false)
	private String status; // open / resolved

	private LocalDateTime resolvedAt;

//	private UUID resolvedBy;  // user who resolved
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resolved_by")
	private User resolvedBy;


	private String attachments; // optional file links
}
