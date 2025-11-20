package com.nimblix.driverdashboard.driverdashboard.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimblix.driverdashboard.driverdashboard.dto.FuelLogDto;
import com.nimblix.driverdashboard.driverdashboard.dto.FuelSummaryDto;
import com.nimblix.driverdashboard.driverdashboard.dto.FuelTrendDto;
import com.nimblix.driverdashboard.driverdashboard.exception.DetailsNotFoundException;
import com.nimblix.driverdashboard.driverdashboard.exception.OperationFailedException;
import com.nimblix.driverdashboard.driverdashboard.model.Driver;
import com.nimblix.driverdashboard.driverdashboard.model.FuelLog;
import com.nimblix.driverdashboard.driverdashboard.model.VehicleTracking;
import com.nimblix.driverdashboard.driverdashboard.repository.DriverRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.FuelRepository;
import com.nimblix.driverdashboard.driverdashboard.repository.VehicleTrackingRepository;

@Service
public class FuelService {

	@Autowired
	private FuelRepository fuelRepository;

	@Autowired
	private VehicleTrackingRepository vehicleTrackingRepository;

	@Autowired
	private DriverRepository driverRepository;

	/** Dashboard summary for a driver */
	public FuelSummaryDto getFuelSummary(String driverId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

		FuelSummaryDto summary = new FuelSummaryDto();

		// ---------------- Current Fuel Status ----------------
		VehicleTracking latestVehicle = vehicleTrackingRepository
				.findTopByDriverIdOrderByTimestampDesc(driver.getId())
				.orElse(null);

		FuelSummaryDto.CurrentFuelStatus status = new FuelSummaryDto.CurrentFuelStatus();
		if (latestVehicle != null) {
			status.setPercentage(latestVehicle.getFuelLevel() != null ? latestVehicle.getFuelLevel() : 0);
			status.setOdometerKm(latestVehicle.getCurrentKm() != null ? latestVehicle.getCurrentKm() : 0);
			status.setEstimatedRangeKm(
					latestVehicle.getFuelLevel() != null ? latestVehicle.getFuelLevel() * 5 : 0
					); // 5 km per liter assumption
		}
		summary.setCurrentFuelStatus(status);

		// ---------------- Monthly Consumption (Current Month Stats) ----------------
		// ---------------- Monthly Consumption ----------------
		YearMonth thisMonth = YearMonth.now();
		LocalDate start = thisMonth.atDay(1);
		LocalDate end = thisMonth.atEndOfMonth();

		// Fetch all fuel logs for this driver in current month
		List<FuelLog> thisMonthLogs = fuelRepository.findByDriver_IdAndDateBetweenOrderByDateAsc(
				driver.getId(),
				Timestamp.valueOf(start.atStartOfDay()),
				Timestamp.valueOf(end.plusDays(1).atStartOfDay().minusNanos(1))
				);



		FuelSummaryDto.MonthlyConsumption consumption = new FuelSummaryDto.MonthlyConsumption();

		if (!thisMonthLogs.isEmpty()) {
			double totalLiters = thisMonthLogs.stream().mapToDouble(FuelLog::getLiters).sum();
			double totalCost = thisMonthLogs.stream().mapToDouble(FuelLog::getCost).sum();

			int minOdo = thisMonthLogs.stream().mapToInt(FuelLog::getOdometerKm).min().orElse(0);
			int maxOdo = thisMonthLogs.stream().mapToInt(FuelLog::getOdometerKm).max().orElse(0);
			int distance = maxOdo - minOdo;

			double mileage = safeDivide(distance, totalLiters);

			consumption.setTotalFuelLiters(totalLiters);
			consumption.setTotalCost(totalCost);
			consumption.setDistanceKm(distance);
			consumption.setMileageKmpl(mileage);
		} else {
			consumption.setTotalFuelLiters(0);
			consumption.setTotalCost(0);
			consumption.setDistanceKm(0);
			consumption.setMileageKmpl(0);
		}


		// Attach to summary
		summary.setMonthlyConsumption(consumption);


		// ---------------- Cost Analysis vs Last Month ----------------
		YearMonth lastMonth = thisMonth.minusMonths(1);
		LocalDate lastStart = lastMonth.atDay(1);
		LocalDate lastEnd = lastMonth.atEndOfMonth();

		List<FuelLog> lastMonthLogs = fuelRepository.findByDriverAndDateBetweenOrderByDateAsc(driver,
				Timestamp.valueOf(lastStart.atStartOfDay()),
				Timestamp.valueOf(lastEnd.atTime(23, 59, 59))
				);

		FuelSummaryDto.CostAnalysis analysis = new FuelSummaryDto.CostAnalysis();
		analysis.setTargetMileage(12); // default target
		double currentMileage = consumption.getMileageKmpl();
		analysis.setFuelEfficiency(currentMileage >= analysis.getTargetMileage() ? "Excellent" : "Poor");

		double change = 0.0;
		if (!lastMonthLogs.isEmpty()) {
			int lastDistance = lastMonthLogs.size() > 1
					? lastMonthLogs.get(lastMonthLogs.size() - 1).getOdometerKm() - lastMonthLogs.get(0).getOdometerKm()
							: (latestVehicle != null && latestVehicle.getCurrentKm() != null
							? latestVehicle.getCurrentKm() - lastMonthLogs.get(0).getOdometerKm()
									: 0);

			double lastLiters = lastMonthLogs.stream().mapToDouble(FuelLog::getLiters).sum();
			double lastMileage = safeDivide(lastDistance, lastLiters);

			if (lastMileage > 0) {
				change = ((currentMileage - lastMileage) / lastMileage) * 100;
				change = Math.round(change * 10.0) / 10.0; // round to 1 decimal
			}
		}
		analysis.setChangeVsLastMonth(change);
		summary.setCostAnalysis(analysis);

		// ---------------- Recent Logs (Last 5 entries) ----------------
		List<FuelLog> recentLogs = fuelRepository.findTop5ByDriverOrderByDateDesc(driver);
		List<FuelLogDto> logDtos = recentLogs.stream()
				.map(log -> new FuelLogDto(
						log.getId(),
						log.getDriver().getId(),
						log.getDate() != null ? log.getDate().toString() : null,
								log.getStation(),
								log.getLiters(),
								log.getCost(),
								log.getOdometerKm()
						))
				.collect(Collectors.toList());
		summary.setRecentLogs(logDtos);

		return summary;
	}

	/** Add new fuel log */
	public FuelLogDto addFuelLog(FuelLogDto fuelLogDto) {
		try {
			Driver driver = driverRepository.findById(fuelLogDto.getDriverId())
					.orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

			FuelLog log = new FuelLog();
			log.setId(UUID.randomUUID().toString());
			if (fuelLogDto.getDate() != null) log.setDate(Timestamp.valueOf(fuelLogDto.getDate()));
			log.setStation(fuelLogDto.getStation());
			log.setLiters(fuelLogDto.getLiters());
			log.setCost(fuelLogDto.getCost());
			log.setOdometerKm(fuelLogDto.getOdometerKm());
			log.setDriver(driver);

			FuelLog saved = fuelRepository.save(log);
			return new FuelLogDto(saved.getId(), saved.getDriver().getId(),
					saved.getDate() != null ? saved.getDate().toString() : null,
							saved.getStation(), saved.getLiters(), saved.getCost(), saved.getOdometerKm());
		} catch (Exception e) {
			throw new OperationFailedException("Failed to save fuel log: " + e.getMessage());
		}
	}

	/** Update an existing fuel log */
	public FuelLogDto updateFuelLog(String id, FuelLogDto dto) {
		FuelLog existing = fuelRepository.findById(id)
				.orElseThrow(() -> new DetailsNotFoundException("Fuel log not found with id " + id));

		if (dto.getDate() != null) existing.setDate(Timestamp.valueOf(dto.getDate()));
		if (dto.getStation() != null) existing.setStation(dto.getStation());
		if (dto.getLiters() != null) existing.setLiters(dto.getLiters());
		if (dto.getCost() != null) existing.setCost(dto.getCost());
		if (dto.getOdometerKm() != null) existing.setOdometerKm(dto.getOdometerKm());
		if (dto.getDriverId() != null) {
			Driver driver = driverRepository.findById(dto.getDriverId())
					.orElseThrow(() -> new DetailsNotFoundException("Driver not found"));
			existing.setDriver(driver);
		}

		FuelLog updated = fuelRepository.save(existing);
		return new FuelLogDto(updated.getId(), updated.getDriver().getId(),
				updated.getDate() != null ? updated.getDate().toString() : null,
						updated.getStation(), updated.getLiters(), updated.getCost(), updated.getOdometerKm());
	}

	/** Delete a fuel log */
	public void deleteFuelLog(String id) {
		if (!fuelRepository.existsById(id))
			throw new DetailsNotFoundException("Fuel log not found with id: " + id);

		fuelRepository.deleteById(id);
	}

	/** Recent fuel logs */
	public List<FuelLogDto> getRecentLogs(String driverId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

		return fuelRepository.findTop5ByDriverOrderByDateDesc(driver).stream()
				.map(log -> new FuelLogDto(
						log.getId(),
						log.getDriver().getId(),
						log.getDate() != null ? log.getDate().toString() : null,
								log.getStation(),
								log.getLiters(),
								log.getCost(),
								log.getOdometerKm()
						))
				.collect(Collectors.toList());
	}

	/** Trends for last 6 months */
	public List<FuelTrendDto> getFuelTrends(String driverId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new DetailsNotFoundException("Driver not found"));

		List<FuelTrendDto> trends = new ArrayList<>();
		for (int i = 5; i >= 0; i--) {
			YearMonth month = YearMonth.now().minusMonths(i);
			LocalDate start = month.atDay(1);
			LocalDate end             = month.atEndOfMonth();

			List<FuelLog> logs = fuelRepository.findByDriverAndDateBetweenOrderByDateAsc(driver,
					Timestamp.valueOf(start.atStartOfDay()),
					Timestamp.valueOf(end.atTime(23, 59, 59))
					);

			if (!logs.isEmpty()) {
				double liters = logs.stream().mapToDouble(FuelLog::getLiters).sum();
				double cost = logs.stream().mapToDouble(FuelLog::getCost).sum();
				int distance = logs.get(logs.size() - 1).getOdometerKm() - logs.get(0).getOdometerKm();
				double mileage = safeDivide(distance, liters);

				trends.add(new FuelTrendDto(month.toString(), liters, cost, distance, mileage));
			} else {
				trends.add(new FuelTrendDto(month.toString(), 0.0, 0.0, 0, 0.0));
			}
		}
		return trends;
	}

	// ---------------- Helper ----------------
	private double safeDivide(double numerator, double denominator) {
		return denominator == 0 ? 0.0 : numerator / denominator;
	}
}

