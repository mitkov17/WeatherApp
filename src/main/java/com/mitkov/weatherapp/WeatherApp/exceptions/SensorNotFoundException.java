package com.mitkov.weatherapp.WeatherApp.exceptions;

public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException() {
        super("Sensor not found!");
    }

    public SensorNotFoundException(Long sensorId) {
        super("Sensor with id " + sensorId + " does not exist!");
    }
}
