package com.mitkov.weatherapp.WeatherApp.exceptions;

public class SensorNotFoundException extends RuntimeException {
    public SensorNotFoundException() {
        super("Sensor not found!");
    }

}
