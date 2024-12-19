package com.mitkov.weatherapp.WeatherApp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.mitkov.weatherapp.WeatherApp.converters.MeasurementConverter;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.services.MeasurementService;
import com.mitkov.weatherapp.WeatherApp.entities.SortParameter;
import com.mitkov.weatherapp.WeatherApp.util.View;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    private final MeasurementConverter measurementConverter;

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO) {

        measurementService.addMeasurement(measurementConverter.convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    @JsonView(View.Summary.class)
    public List<Measurement> getAllMeasurements() {
        return measurementService.getAllMeasurements();
    }

    @GetMapping("/rainyDays")
    @JsonView(View.Summary.class)
    public List<Measurement> getRainyDays() {
        return measurementService.getRainyDays();
    }

    @GetMapping("/filter")
    @JsonView(View.Summary.class)
    public List<Measurement> filterMeasurements(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                                @RequestParam(required = false) Double min,
                                                @RequestParam(required = false) Double max) {

        return measurementService.filterMeasurements(measurementUnit, min, max);
    }

    @GetMapping("/sort")
    @JsonView(View.Summary.class)
    public List<Measurement> sortMeasurements(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                              @RequestParam(required = false) Boolean ascending,
                                              @RequestParam(required = false) SortParameter sortParam) {

        return measurementService.sortMeasurements(measurementUnit, ascending, sortParam);
    }

    @GetMapping("/statistics")
    public List<MeasurementsStatisticsDTO> getMeasurementStatistics(@RequestParam(required = false) MeasurementUnit measurementUnit,
                                                                    @RequestParam(required = false) String startDate,
                                                                    @RequestParam(required = false) String endDate) {

        return measurementService.getMeasurementStatistics(measurementUnit, startDate, endDate);
    }

    @GetMapping("/range")
    @JsonView(View.Summary.class)
    public List<Measurement> getMeasurementsByTimeRange(@RequestParam String startDate,
                                                        @RequestParam String endDate) {

        return measurementService.getMeasurementsByTimeRange(startDate, endDate);
    }

    @GetMapping("/paginated")
    public Page<Measurement> getPaginatedMeasurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return measurementService.getPaginatedMeasurements(page, size);
    }

}
