package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "safety_checklist")
public class SafetyChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "check_item", nullable = false, columnDefinition = "TEXT")
    private String checkItem;

    @Column(name = "check_time")
    private LocalTime checkTime;

    private String status = "pending";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_tracking_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private VehicleTracking vehicleTracking;

    @OneToMany(mappedBy = "safetyChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<ChecklistImage> checklistImages;
}
