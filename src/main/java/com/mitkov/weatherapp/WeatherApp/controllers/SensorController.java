package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.SensorConverter;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotCreatedException;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    private final SensorConverter sensorConverter;

    @Autowired
    public SensorController(SensorService sensorService, SensorConverter sensorConverter) {
        this.sensorService = sensorService;
        this.sensorConverter = sensorConverter;
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> saveSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new SensorNotCreatedException(errorMsg.toString());
        }

        sensorService.saveSensor(sensorConverter.convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }
}
