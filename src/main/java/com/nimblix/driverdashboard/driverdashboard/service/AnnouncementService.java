package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.repository.AnnouncementRepository;
import com.nimblix.driverdashboard.driverdashboard.dto.AnnouncementInfoDto;
import com.nimblix.driverdashboard.driverdashboard.model.Announcement;
import com.nimblix.driverdashboard.driverdashboard.exception.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    /** Get latest 5 announcements */
    public List<AnnouncementInfoDto> getRecentAnnouncements() {
        return announcementRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /** Get all announcements */
    public List<AnnouncementInfoDto> getAllAnnouncements() {
        return announcementRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /** Create a new announcement */
    public AnnouncementInfoDto createAnnouncement(AnnouncementInfoDto dto) {
        try {
            Announcement entity = new Announcement();

            entity.setId(UUID.randomUUID().toString());
            entity.setTitle(dto.getTitle());
            entity.setDescription(dto.getDescription()); // ✅ changed from setContent
            entity.setPriority(dto.getPriority() != null ? dto.getPriority() : "normal");
            entity.setType(dto.getType() != null ? dto.getType() : "Transport");
            entity.setTargetClass(dto.getTargetClass());
            entity.setCreatedAt(LocalDateTime.now());

            Announcement saved = announcementRepository.save(entity);
            return mapToDto(saved);
        } catch (Exception e) {
            throw new OperationFailedException("Unable to save announcement");
        }
    }

    /** Update an announcement */
    public AnnouncementInfoDto updateAnnouncement(String id, AnnouncementInfoDto dto) {
        Announcement existing = announcementRepository.findById(id)
                .orElseThrow(() -> new DetailsNotFoundException("Announcement not found with id " + id));
        try {
            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription()); // ✅ changed from setContent
            existing.setPriority(dto.getPriority() != null ? dto.getPriority() : existing.getPriority());
            existing.setType(dto.getType() != null ? dto.getType() : existing.getType());
            existing.setTargetClass(dto.getTargetClass());

            Announcement updated = announcementRepository.save(existing);
            return mapToDto(updated);
        } catch (Exception e) {
            throw new OperationFailedException("Unable to update announcement with id " + id);
        }
    }

    /** Delete an announcement */
    public void deleteAnnouncement(String id) {
        if (!announcementRepository.existsById(id)) {
            throw new DetailsNotFoundException("Announcement not found with id " + id);
        }
        try {
            announcementRepository.deleteById(id);
        } catch (Exception e) {
            throw new OperationFailedException("Unable to delete announcement with id " + id);
        }
    }

    /** Mapper: Entity -> DTO */
    private AnnouncementInfoDto mapToDto(Announcement entity) {
        AnnouncementInfoDto dto = new AnnouncementInfoDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription()); // ✅ changed from getContent
        dto.setType(entity.getType());
        dto.setPriority(entity.getPriority());
        dto.setTargetClass(entity.getTargetClass());
        dto.setDate(entity.getCreatedAt());

        return dto;
    }
}
