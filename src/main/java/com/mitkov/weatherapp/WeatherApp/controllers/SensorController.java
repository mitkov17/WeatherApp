package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.SensorConverter;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    private final SensorConverter sensorConverter;

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> saveSensor(@RequestBody @Valid SensorDTO sensorDTO) {

        sensorService.saveSensor(sensorConverter.convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping("/{id}")
    public Sensor getSensorById(@PathVariable("id") Long sensorId) {
        return sensorService.findById(sensorId);
    }

    @GetMapping("/{id}/measurements")
    public List<Measurement> getMeasurementsBySensor(@PathVariable("id") Long sensorId) {
        return sensorService.getMeasurementsBySensor(sensorId);
    }

    @GetMapping("/search")
    public List<Sensor> searchForSensor(@RequestParam String template) {
        return sensorService.searchForSensor(template);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSensor(@PathVariable("id") Long sensorId) {
        sensorService.deleteSensor(sensorId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<HttpStatus> updateSensorName(@PathVariable("id") Long sensorId, @RequestParam String newName) {
        sensorService.updateSensorName(sensorId, newName);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
