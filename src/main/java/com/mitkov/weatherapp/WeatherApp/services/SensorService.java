package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void saveSensor(Sensor sensor) {
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

    @Transactional
    public void deleteSensor(Long sensorId) {
        sensorRepository.delete(sensorRepository.findById(sensorId).orElseThrow(() -> new SensorNotFoundException(sensorId)));
    }

    public List<Sensor> searchForSensor(String template) {
        return sensorRepository.findByNameContainingIgnoreCase(template);
    }

    @Transactional
    public void updateSensorName(Long sensorId, String newName) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new SensorNotFoundException(sensorId));
        sensor.setName(newName);
        sensorRepository.save(sensor);
    }
}
