package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.MeasurementConverter;
import com.mitkov.weatherapp.WeatherApp.converters.SensorConverter;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementGetDTO;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    private final SensorConverter sensorConverter;

    private final MeasurementConverter measurementConverter;

    @Operation(description = "Method that returns all the sensors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success|OK"),
    })
    @GetMapping()
    public List<SensorDTO> getAllSensors() {
        List<Sensor> sensors = sensorService.getAllSensors();
        return sensors.stream().map(sensorConverter::convertToSensorDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDTO getSensorById(@PathVariable("id") Long sensorId) {
        return sensorConverter.convertToSensorDTO(sensorService.findById(sensorId));
    }

    @GetMapping("/{id}/measurements")
    public List<MeasurementGetDTO> getMeasurementsBySensor(@PathVariable("id") Long sensorId) {
        List<Measurement> measurements = sensorService.getMeasurementsBySensor(sensorId);
        return measurements.stream().map(measurementConverter::convertToMeasurementGetDTO).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<SensorDTO> searchForSensor(@RequestParam String template) {
        List<Sensor> sensors = sensorService.searchForSensor(template);
        return sensors.stream().map(sensorConverter::convertToSensorDTO).collect(Collectors.toList());
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<String> updateSensorName(@PathVariable("id") Long sensorId, @RequestParam String newName) {
        sensorService.updateSensorName(sensorId, newName);

        return ResponseEntity.ok("The sensor has been updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSensor(@PathVariable("id") Long sensorId) {
        sensorService.deleteSensor(sensorId);

        return ResponseEntity.ok("The sensor has been deleted successfully");
    }

}
