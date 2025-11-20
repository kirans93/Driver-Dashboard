package com.nimblix.driverdashboard.driverdashboard.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fuel_logs")
public class FuelLog {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", columnDefinition = "CHAR(36)")
	@JsonProperty("id")
	private String id;


	// Reference to Driver
	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
	private Driver driver;

	@Column(nullable = false)
	@JsonProperty("date")
	private Timestamp date;

	@Column(nullable = false)
	@JsonProperty("station")
	private String station;

	@Column(nullable = false)
	@JsonProperty("liters")
	private Double liters;

	@Column(nullable = false)
	@JsonProperty("cost")
	private Double cost;

	@Column(name = "odometer_km", nullable = false)
	@JsonProperty("odometer_km")
	private Integer odometerKm;

	@ManyToOne
	@JoinColumn(name = "vehicle_tracking_id")
    @JsonBackReference
	private VehicleTracking vehicleTracking;




}
