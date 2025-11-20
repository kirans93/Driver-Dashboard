package com.nimblix.driverdashboard.driverdashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data                  
@NoArgsConstructor      
@AllArgsConstructor    
public class AnnouncementInfoDto {

    private String id;              // unique identifier
    private String title;
    private String description;     // maps to `content` in entity
    private String type;            // "Transport", "Academic", "General"
    private String priority;        // "High", "Normal", "Low"
    private String targetClass;     // optional
    private LocalDateTime date;     // maps to `createdAt` in entity
}
