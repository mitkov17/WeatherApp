package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.InvalidSensorNameException;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorAlreadyExistsException;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Transactional
    public void saveSensor(Sensor sensor) {
        sensorRepository.findByName(sensor.getName())
                .ifPresent(existingSensor -> {
                    throw new SensorAlreadyExistsException(sensor.getName());
                });

        sensorRepository.save(sensor);
    }


    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor findById(Long sensorId) {
        return sensorRepository.findById(sensorId).orElseThrow(() -> new SensorNotFoundException(sensorId));
    }

    public List<Measurement> getMeasurementsBySensor(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException(sensorId));
        return sensor.getMeasurements();
    }

    public List<Sensor> searchForSensor(String template) {
        return sensorRepository.findByNameContainingIgnoreCase(template);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSensor(Long sensorId) {
        sensorRepository.delete(sensorRepository.findById(sensorId).orElseThrow(() -> new SensorNotFoundException(sensorId)));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updateSensorName(Long sensorId, String newName) {
        if (newName.length() < 2 || newName.length() > 100) {
            throw new InvalidSensorNameException("Name length should be between 2 and 100 symbols");
        }
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new SensorNotFoundException(sensorId));
        sensor.setName(newName);
        sensorRepository.save(sensor);
    }
}
