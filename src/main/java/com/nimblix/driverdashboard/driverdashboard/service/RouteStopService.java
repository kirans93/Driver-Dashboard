package com.nimblix.driverdashboard.driverdashboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.RouteStopDto;
import com.nimblix.driverdashboard.driverdashboard.model.BusRoute;
import com.nimblix.driverdashboard.driverdashboard.model.RouteStop;
import com.nimblix.driverdashboard.driverdashboard.repository.BusRouteRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.RouteStopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteStopService {

    private final RouteStopRepository routeStopRepository;
    private final BusRouteRepository busRouteRepository;

    /** Convert RouteStop entity to RouteStopDto */
    private RouteStopDto toDto(RouteStop stop) {
        RouteStopDto dto = new RouteStopDto();
        dto.setId(stop.getId());
        dto.setStopName(stop.getStopName());
        dto.setScheduledTime(stop.getScheduledTime());
        dto.setStudentCount(stop.getStudentCount());
        dto.setStatus(stop.getStatus());
        dto.setBusRouteId(stop.getBusRoute().getId());
        return dto;
    }

    /** Convert RouteStopDto to RouteStop entity */
    private RouteStop toEntity(RouteStopDto dto, BusRoute busRoute) {
        RouteStop stop = new RouteStop();
        stop.setStopName(dto.getStopName());
        stop.setScheduledTime(dto.getScheduledTime());
        stop.setStudentCount(dto.getStudentCount());
        stop.setStatus(dto.getStatus());
        stop.setBusRoute(busRoute);
        return stop;
    }

    /** Fetch all stops for a given bus route as DTOs */
    public List<RouteStopDto> getStopsByBusRoute(String busRouteId) {
        return routeStopRepository
                .findByBusRoute_IdOrderByScheduledTimeAsc(busRouteId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Add a new stop to a bus route using DTO */
    public RouteStopDto addRouteStop(String busRouteId, RouteStopDto routeStopDto) {
        BusRoute busRoute = busRouteRepository.findById(busRouteId)
                .orElseThrow(() -> new RuntimeException("BusRoute not found with id: " + busRouteId));

        RouteStop stop = toEntity(routeStopDto, busRoute);
        RouteStop saved = routeStopRepository.save(stop);

        return toDto(saved);
    }
}
