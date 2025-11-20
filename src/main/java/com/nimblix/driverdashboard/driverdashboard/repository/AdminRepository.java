package com.nimblix.driverdashboard.driverdashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimblix.driverdashboard.driverdashboard.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
    // Example: find admin by userId (foreign key to non-teaching staff)
	/**
     * Find an Admin by the associated userId.
     * Make sure to pass a UUID, not a String.
     *
     * @param userId UUID of the user
     * @return Optional<Admin>
     */
    Optional<Admin> findByUserId(String userId);
}
