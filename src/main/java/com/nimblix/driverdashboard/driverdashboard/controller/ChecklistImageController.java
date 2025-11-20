package com.nimblix.driverdashboard.driverdashboard.controller;

import com.nimblix.driverdashboard.driverdashboard.model.ChecklistImage;
import com.nimblix.driverdashboard.driverdashboard.service.ChecklistImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checklist-images")
public class ChecklistImageController {

    private final ChecklistImageService imageService;

    public ChecklistImageController(ChecklistImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * API: Upload a checklist image
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/checklist-images
     * FORM DATA:
     *   - checklistId: String
     *   - file: MultipartFile (image file to upload)
     * RESPONSE: ChecklistImage object if successful, or error message
     */
    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("checklistId") String checklistId,
                                         @RequestParam("file") MultipartFile file) {
        try {
            ChecklistImage image = imageService.saveImage(checklistId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to upload image: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * API: Get all images for a given checklist
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/checklist-images/{checklistId}
     * PATH PARAM: checklistId
     * RESPONSE: List of ChecklistImage objects, or 204 No Content if none exist
     */
    @GetMapping("/{checklistId}")
    public ResponseEntity<List<ChecklistImage>> getChecklistImages(@PathVariable String checklistId) {
        List<ChecklistImage> images = imageService.getImagesByChecklist(checklistId);
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    /**
     * API: Download a checklist image by ID
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/checklist-images/download/{imageId}
     * PATH PARAM: imageId
     * RESPONSE: Image file as byte[] with content-disposition attachment
     */
    @GetMapping("/download/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String imageId) {
        try {
            byte[] imageData = imageService.getImageFile(imageId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // TODO: Adjust dynamically if other formats are supported
            headers.setContentDispositionFormData("attachment", "checklist-image.jpg");
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API: Delete an image by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/api/checklist-images/{imageId}
     * PATH PARAM: imageId
     * RESPONSE: JSON message confirming deletion or error if not found
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
