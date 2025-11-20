package com.nimblix.driverdashboard.driverdashboard.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.model.StudentTransportation;
import com.nimblix.driverdashboard.driverdashboard.dto.DriverDailyTransportStatsDto;
import com.nimblix.driverdashboard.driverdashboard.repository.StudentTransportationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentTransportationService {

    private final StudentTransportationRepository transportationRepository;

    public StudentTransportation createTransportationRecord(StudentTransportation transportation) {
        // Set transport date if null
        if (transportation.getTransportDate() == null) {
            transportation.setTransportDate(LocalDate.now());
        }

        // Set onTime default if null
        if (transportation.getOnTime() == null) {
            transportation.setOnTime(true); // or false depending on your logic
        }

        return transportationRepository.save(transportation);
    }


    public List<StudentTransportation> getTransportationsByDriver(String driverId, String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return transportationRepository.findByDriverIdAndTransportDateBetween(driverId, start, end);
        }
        // Return all records for driver if no date range specified
        return transportationRepository.findByDriverId(driverId);
    }

    public List<StudentTransportation> getTransportationsByStudent(String studentId, String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return transportationRepository.findByStudentIdAndTransportDateBetween(studentId, start, end);
        }
        return transportationRepository.findByStudentId(studentId);
    }

    public Map<String, Object> getTransportationStats(String driverId, String month) {
        LocalDate startDate;
        LocalDate endDate;
        
        if (month != null) {
            // Parse specific month (format: "2024-03")
            startDate = LocalDate.parse(month + "-01");
            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        } else {
            // Default to current month
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }

        long uniqueStudents = transportationRepository.countUniqueStudentsTransported(driverId, startDate, endDate);
        long totalTrips = transportationRepository.countTripsCompleted(driverId, startDate, endDate);

        Map<String, Object> stats = new HashMap<>();
        stats.put("driverId", driverId);
        stats.put("period", startDate.getMonth().toString() + " " + startDate.getYear());
        stats.put("uniqueStudentsTransported", uniqueStudents);
        stats.put("totalTripsCompleted", totalTrips);
        stats.put("averageStudentsPerTrip", totalTrips > 0 ? (double) uniqueStudents / totalTrips : 0);

        return stats;
    }

	public DriverDailyTransportStatsDto getTodayDriverDailyStats(String driverId) {
		LocalDate today = LocalDate.now();
		long assigned = transportationRepository.countDistinctAssignedForDate(driverId, today);
		long picked = transportationRepository.countPickedUpForDate(driverId, today);
		long dropped = transportationRepository.countDroppedOffForDate(driverId, today);
		int pendingPickup = (int) Math.max(0, assigned - picked);
		int pendingDrop = (int) Math.max(0, picked - dropped);
		return new DriverDailyTransportStatsDto(
			driverId,
			today.toString(),
			(int) assigned,
			(int) picked,
			(int) dropped,
			pendingPickup,
			pendingDrop
		);
	}
}