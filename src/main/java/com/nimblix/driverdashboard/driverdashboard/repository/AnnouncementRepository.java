package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.Announcement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
	
	 List<Announcement> findTop5ByOrderByCreatedAtDesc();
	
}
