package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
	
	
//	INSERT INTO students (id, created_at, name, parent_contact, roll_number, section, class, updated_at, bus_route_id, user_id)
//	VALUES (UUID(), NOW(), 'Raj Kumar', '9876543210', '101', 'A', '10', NOW(), 'fe09396d-851d-4e46-b6ad-254da03ad499', NULL),
//	(UUID(), NOW(), 'Shyam Sharma', '9876543222', '102', 'A', '10', NOW(), 'fe09396d-851d-4e46-b6ad-254da03ad499', NULL);


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = true , insertable = false)
//    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "roll_number", nullable = false)
    private String rollNumber;

    @Column(name = "class", nullable = false)
    private String studentClass;

    @Column(name = "section", nullable = false)
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_route_id")
    private BusRoute busRoute;

    @Column(name = "parent_contact")
    private String parentContact;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendanceRecords;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
