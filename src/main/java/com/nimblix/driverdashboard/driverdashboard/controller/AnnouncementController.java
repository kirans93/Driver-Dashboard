package com.nimblix.driverdashboard.driverdashboard.controller;

import com.nimblix.driverdashboard.driverdashboard.service.*;
import com.nimblix.driverdashboard.driverdashboard.dto.AnnouncementInfoDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    // ---------------- GET ----------------

    /**
     * API: Get latest 5 announcements
     * METHOD: GET
     * URL: http://localhost:8080/api/announcements/recent
     * RESPONSE: List of last 5 AnnouncementInfoDto
     */
    @GetMapping("/recent")
    public ResponseEntity<List<AnnouncementInfoDto>> getRecentAnnouncements() {
        return ResponseEntity.ok(announcementService.getRecentAnnouncements());
    }

    /**
     * API: Get all announcements
     * METHOD: GET
     * URL: http://localhost:8080/api/announcements
     * RESPONSE: List of all AnnouncementInfoDto
     */
    @GetMapping
    public ResponseEntity<List<AnnouncementInfoDto>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    // ---------------- POST ----------------

    /**
     * API: Create a new announcement
     * METHOD: POST
     * URL: http://localhost:8080/api/announcements
     * BODY: AnnouncementInfoDto JSON
     * RESPONSE: Success message
     */
    @PostMapping
    public ResponseEntity<String> createAnnouncement(@RequestBody AnnouncementInfoDto dto) {
        announcementService.createAnnouncement(dto);
        return ResponseEntity.ok("✅ Successfully added announcement!");
    }

    // ---------------- PUT ----------------

    /**
     * API: Update an announcement by ID
     * METHOD: PUT
     * URL: http://localhost:8080/api/announcements/{id}
     * BODY: AnnouncementInfoDto JSON
     * RESPONSE: Success message
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateAnnouncement(
            @PathVariable String id,
            @RequestBody AnnouncementInfoDto dto) {
        announcementService.updateAnnouncement(id, dto);
        return ResponseEntity.ok("✅ Successfully updated announcement with ID: " + id);
    }

    // ---------------- DELETE ----------------

    /**
     * API: Delete an announcement by ID
     * METHOD: DELETE
     * URL: http://localhost:8080/api/announcements/{id}
     * RESPONSE: Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable String id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("✅ Successfully deleted announcement with ID: " + id);
    }
}
