package com.nimblix.driverdashboard.driverdashboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimblix.driverdashboard.driverdashboard.model.EmergencyContact;
import com.nimblix.driverdashboard.driverdashboard.service.EmergencyContactService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService service;

    public EmergencyContactController(EmergencyContactService service) {
        this.service = service;
    }

    /**
     * API: Get all emergency contacts
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/emergency-contacts
     * RESPONSE: List of EmergencyContact objects, or 204 if none found
     */
    @GetMapping
    public ResponseEntity<List<EmergencyContact>> list() {
        List<EmergencyContact> contacts = service.findAll();
        if (contacts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(contacts);
        }
        return ResponseEntity.ok(contacts);
    }

    /**
     * API: Get emergency contact by ID
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/emergency-contacts/{id}
     * PATH PARAM: id
     * RESPONSE: EmergencyContact object, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * API: Get emergency contacts by department
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/emergency-contacts/department/{department}
     * PATH PARAM: department
     * RESPONSE: List of EmergencyContact objects for the given department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmergencyContact>> getByDepartment(@PathVariable String department) {
        List<EmergencyContact> contacts = service.findByDepartment(department);
        return ResponseEntity.ok(contacts);
    }

    /**
     * API: Get emergency contacts by role
     * METHOD: GET
     * FULL URL: http://localhost:8080/api/emergency-contacts/role/{role}
     * PATH PARAM: role
     * RESPONSE: List of EmergencyContact objects for the given role
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<EmergencyContact>> getByRole(@PathVariable String role) {
        List<EmergencyContact> contacts = service.findByRole(role);
        return ResponseEntity.ok(contacts);
    }

    /**
     * API: Create a new emergency contact
     * METHOD: POST
     * FULL URL: http://localhost:8080/api/emergency-contacts
     * BODY: JSON object containing EmergencyContact details
     * RESPONSE: The created EmergencyContact object
     */
    @PostMapping
    public ResponseEntity<EmergencyContact> create(@RequestBody EmergencyContact contact) {
        EmergencyContact savedContact = service.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    /**
     * API: Update an existing emergency contact
     * METHOD: PUT
     * FULL URL: http://localhost:8080/api/emergency-contacts/{id}
     * PATH PARAM: id
     * BODY: JSON object containing updated EmergencyContact details
     * RESPONSE: The updated EmergencyContact object, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContact> update(@PathVariable String id, @RequestBody EmergencyContact contact) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        contact.setId(id);
        EmergencyContact updatedContact = service.save(contact);
        return ResponseEntity.ok(updatedContact);
    }

    /**
     * API: Delete an emergency contact by ID
     * METHOD: DELETE
     * FULL URL: http://localhost:8080/api/emergency-contacts/{id}
     * PATH PARAM: id
     * RESPONSE: JSON confirmation message, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Contact deleted successfully"));
    }
}
