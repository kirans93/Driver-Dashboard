package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fuel_entries")
public class FuelEntry {

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

    private Double fuelAddedLitres;   // keep as Double to match DTO

    private Double fuelCost;

    private Double odometerKm;

    private LocalDateTime createdAt;
}
