package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.MeasurementConverter;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementGetDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.SortParameter;
import com.mitkov.weatherapp.WeatherApp.services.MeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    private final MeasurementConverter measurementConverter;

    @PostMapping("/add")
    public ResponseEntity<String> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO) {

        measurementService.addMeasurement(measurementConverter.convertToMeasurement(measurementDTO));

        return ResponseEntity.ok("The measurement has been added successfully");
    }

    @GetMapping
    public List<MeasurementGetDTO> getAllMeasurements() {
        List<Measurement> measurements = measurementService.getAllMeasurements();
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDays")
    public List<MeasurementGetDTO> getRainyDays() {
        List<Measurement> measurements = measurementService.getRainyDays();
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<MeasurementGetDTO> filterMeasurements(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                                @RequestParam(required = false) Double min,
                                                @RequestParam(required = false) Double max) {

        List<Measurement> measurements = measurementService.filterMeasurements(measurementUnit, min, max);
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/sort")
    public List<MeasurementGetDTO> sortMeasurements(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                              @RequestParam(required = false) Boolean ascending,
                                              @RequestParam(required = false) SortParameter sortParam) {

        List<Measurement> measurements = measurementService.sortMeasurements(measurementUnit, ascending, sortParam);
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/statistics")
    public List<MeasurementsStatisticsDTO> getMeasurementStatistics(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                                                    @RequestParam(required = false) String startDate,
                                                                    @RequestParam(required = false) String endDate) {

        return measurementService.getMeasurementStatistics(measurementUnit, startDate, endDate);
    }

    @GetMapping("/range")
    public List<MeasurementGetDTO> getMeasurementsByTimeRange(@RequestParam String startDate,
                                                        @RequestParam String endDate) {

        List<Measurement> measurements = measurementService.getMeasurementsByTimeRange(startDate, endDate);
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/paginated")
    public Page<MeasurementGetDTO> getPaginatedMeasurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Measurement> measurements = measurementService.getPaginatedMeasurements(page, size);

        return measurements.map(measurementConverter::convertToMeasurementGetDTO);
    }

}
