package com.nimblix.driverdashboard.driverdashboard.model;

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


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "maintenance_items")
public class MaintenanceItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("category")
    private String category;

    @Column(name = "service_type")
    @JsonProperty("service_type")
    private String serviceType;

    @JsonProperty("action")
    private String action;

    @JsonProperty("status")
    private String status;

    @Column(name = "estimated_cost")
    @JsonProperty("estimated_cost")
    private Double estimatedCost;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_tracking_id" , nullable = false , insertable = true)
    @JsonBackReference
    private VehicleTracking vehicleTracking;
    
    
}
