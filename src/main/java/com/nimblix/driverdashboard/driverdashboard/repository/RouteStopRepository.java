package com.nimblix.driverdashboard.driverdashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimblix.driverdashboard.driverdashboard.model.RouteStop;

import jakarta.transaction.Transactional;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, String> {
    List<RouteStop> findByBusRouteIdOrderByScheduledTimeAsc(String busRouteId);
    
    List<RouteStop> findByBusRoute_IdOrderByScheduledTimeAsc(String busRouteId);
    
  
    
    @Modifying
    @Transactional
    @Query("UPDATE RouteStop r SET r.status = 'UPCOMING' WHERE r.busRoute.id = :busRouteId")
    void resetAllStatusesToUpcoming(String busRouteId);

}
