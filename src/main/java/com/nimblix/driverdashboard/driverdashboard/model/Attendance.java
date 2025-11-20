package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;
    
    @Column(name = "student_id", nullable = false)
    private String studentId;
    
    @Column(name = "class_id")
    private String classId;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @Column(name = "status", nullable = false)
    private String status; // PRESENT, ABSENT, LATE, LEAVE
    
    @Column(name = "submitted_by")
    private String submittedBy;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Student student;
    
    
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (date == null) {
            date = LocalDate.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = "PRESENT";
        }
    }
}