package com.nimblix.driverdashboard.driverdashboard.service;

import com.nimblix.driverdashboard.driverdashboard.model.ChecklistImage;
import com.nimblix.driverdashboard.driverdashboard.model.SafetyChecklist;
import com.nimblix.driverdashboard.driverdashboard.repository.ChecklistImageRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.SafetyChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistImageService {

    private final ChecklistImageRepository imageRepository;
    private final SafetyChecklistRepository checklistRepository;

    // Define upload directory (can also move to application.properties)
    private final Path rootLocation = Paths.get("uploads/checklist-images");

    /**
     * Save a checklist image
     */
    public ChecklistImage saveImage(String checklistId, MultipartFile file) throws IOException {

        // Create directory if not exists
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        // Verify checklist exists
        SafetyChecklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new RuntimeException("Checklist not found with id: " + checklistId));

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String newFilename = UUID.randomUUID().toString() + fileExtension; // generate string UUID

        // Save file to filesystem
        Path destinationFile = rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        // Save metadata to database
        ChecklistImage image = new ChecklistImage();
        image.setId(UUID.randomUUID().toString()); // generate UUID as string
        image.setSafetyChecklist(checklist);
        image.setFileName(originalFilename);
        image.setFilePath(destinationFile.toString());
        image.setCreatedAt(LocalDateTime.now());

        return imageRepository.save(image);
    }


    /**
     * Get all images for a checklist
     */
    public List<ChecklistImage> getImagesByChecklist(String checklistId) {
        return imageRepository.findBySafetyChecklistId(checklistId); // corrected repository query
    }

    /**
     * Delete a checklist image
     */
    public void deleteImage(String imageId) {
        Optional<ChecklistImage> image = imageRepository.findById(imageId);
        if (image.isPresent()) {
            try {
                // Delete file from filesystem
                Files.deleteIfExists(Paths.get(image.get().getFilePath()));
                // Delete from database
                imageRepository.deleteById(imageId);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image file", e);
            }
        } else {
            throw new RuntimeException("Image not found with id: " + imageId);
        }
    }

    /**
     * Get image file as byte array
     */
    public byte[] getImageFile(String imageId) throws IOException {
        ChecklistImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        Path imagePath = Paths.get(image.getFilePath());
        return Files.readAllBytes(imagePath);
    }
}
