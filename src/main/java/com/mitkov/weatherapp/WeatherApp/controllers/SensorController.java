package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.AppUserConverter;
import com.mitkov.weatherapp.WeatherApp.converters.SensorConverter;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.dto.SensorUserCreationDTO;
import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.RegistrationService;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import com.mitkov.weatherapp.WeatherApp.util.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    private final SensorConverter sensorConverter;

    private final RegistrationService registrationService;

    private final AppUserConverter appUserConverter;

    @PostMapping("/register-sensor")
    public ResponseEntity<HttpStatus> registerSensorUser(@RequestBody @Valid SensorUserCreationDTO sensorUserCreationDTO) {
        AppUser appUser = appUserConverter.convertToAppUser(sensorUserCreationDTO);

        AppUser savedUser = registrationService.register(appUser);

        Sensor sensor = sensorConverter.convertToSensor(sensorUserCreationDTO.getSensorDTO());
        sensor.setCreatedBy(savedUser);
        sensorService.saveSensor(sensor);

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
