package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "non_teaching_staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false)
	private String id;



	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "employee_id", nullable = false)
	private String employeeId;

	@Column(name = "department", nullable = false)
	private String department;

	@Column(name = "designation", nullable = false)
	private String designation;

	@Column(name = "shift")
	private String shift;

	@Column(name = "phone")
	private String phone;

	@Column(name = "vehicle_assigned")
	private String vehicleAssigned;

	@Column(name = "joining_date")
	private LocalDateTime joiningDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	private Admin admin;

    

	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GPSTracking> gpsTrackings;

	@OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VehicleTracking> vehicleTrackings;

	@ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
	@JoinColumn(name = "announcement_id")
	private Announcement announcement;
	

	@Column(name = "license_expiry_date")
	@JsonProperty("license_expiry_date")
	private LocalDateTime licenseExpiryDate;

	@Column(name = "duty_status")
	@JsonProperty("duty_status")
	private String dutyStatus; // "On Duty", "Off Duty"

	@Column(name = "experience")
	@JsonProperty("experience")
	private Integer experience;
	
	



}