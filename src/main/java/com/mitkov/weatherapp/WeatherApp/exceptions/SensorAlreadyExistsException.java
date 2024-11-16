package com.mitkov.weatherapp.WeatherApp.exceptions;

public class SensorAlreadyExistsException extends RuntimeException {

    public SensorAlreadyExistsException(String sensorName) {
        super("Sensor \"" + sensorName + "\" already exists!");
    }
}
