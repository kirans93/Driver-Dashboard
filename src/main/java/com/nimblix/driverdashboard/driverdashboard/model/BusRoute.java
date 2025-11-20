package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus_routes")
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private String id; // UUID as String (CHAR 36)

    @Column(name = "route_number", nullable = false)
    private String routeNumber;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    private String currentLocation;
    private String nextStop;
    private String eta;
    private String status;

    // Vehicle reference
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    // Time fields
    private LocalTime startTime;
    private LocalTime endTime;

    // Distance in kilometers
    private Double distance;

    // Estimated arrival at school
    @Column(name = "eta_school")
    private String etaSchool;

    // Students on this route
    @OneToMany(mappedBy = "busRoute", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Student> students = new ArrayList<>();


    // Stops on this route
    @OneToMany(mappedBy = "busRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RouteStop> stops = new ArrayList<>();
    
    private String vehicleNumber;
    
    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;
    
    
    
    
    
    

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}
