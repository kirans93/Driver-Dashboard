package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    private String department;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

   
}
