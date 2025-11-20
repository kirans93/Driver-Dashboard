package com.nimblix.driverdashboard.driverdashboard.model;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dashboard_summary")
public class DashboardSummary {
	
    @Id
    @Column(name = "driver_id")
    private String driverId;

	@OneToOne
    @MapsId
    @JoinColumn(name = "driver_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
    private Driver driver; 

	private Double overallRating;
	private String onTimePerformance;
	private String safetyScore;
	private String fuelEfficiency;
	private Integer tripsCompleted;
	private Integer kmDriven;
	private Integer fuelUsed;
	private Integer studentsTransported;
	
	@ElementCollection
	@CollectionTable(
	    name = "dashboard_achievements",
	    joinColumns = @JoinColumn(name = "driver_id")
	)
	@MapKeyColumn(name = "name")
	@Column(name = "completed")
	private Map<String, Boolean> recentAchievements = new HashMap<>();

}