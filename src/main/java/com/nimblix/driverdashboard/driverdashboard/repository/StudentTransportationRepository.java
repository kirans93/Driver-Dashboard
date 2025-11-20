package com.nimblix.driverdashboard.driverdashboard.repository;

import com.nimblix.driverdashboard.driverdashboard.model.StudentTransportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentTransportationRepository extends JpaRepository<StudentTransportation, Long> {
    
    // Add these missing methods:
    List<StudentTransportation> findByDriverId(String driverId);
    
    List<StudentTransportation> findByStudentId(String studentId);
    
    List<StudentTransportation> findByStudentIdAndTransportDateBetween(String studentId,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate);
    
    // Your existing methods...
    @Query("SELECT COUNT(DISTINCT st.studentId) FROM StudentTransportation st " +
           "WHERE st.driverId = :driverId AND st.transported = true " +
           "AND st.transportDate BETWEEN :startDate AND :endDate")
    long countUniqueStudentsTransported(@Param("driverId") String driverId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(st) FROM StudentTransportation st " +
           "WHERE st.driverId = :driverId AND st.transported = true " +
           "AND st.transportDate BETWEEN :startDate AND :endDate")
    long countTripsCompleted(@Param("driverId") String driverId,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate);
    
    List<StudentTransportation> findByDriverIdAndTransportDateBetween(String driverId, 
                                                                     LocalDate startDate, 
                                                                     LocalDate endDate);

	@Query("SELECT COUNT(DISTINCT st.studentId) FROM StudentTransportation st " +
	       "WHERE st.driverId = :driverId AND st.transportDate = :date")
	long countDistinctAssignedForDate(@Param("driverId") String driverId,
	                                 @Param("date") LocalDate date);

	@Query("SELECT COUNT(st) FROM StudentTransportation st " +
	       "WHERE st.driverId = :driverId AND st.transportDate = :date AND st.pickupTime IS NOT NULL")
	long countPickedUpForDate(@Param("driverId") String driverId,
	                         @Param("date") LocalDate date);

	@Query("SELECT COUNT(st) FROM StudentTransportation st " +
	       "WHERE st.driverId = :driverId AND st.transportDate = :date AND st.dropoffTime IS NOT NULL")
	long countDroppedOffForDate(@Param("driverId") String driverId,
	                           @Param("date") LocalDate date);
}