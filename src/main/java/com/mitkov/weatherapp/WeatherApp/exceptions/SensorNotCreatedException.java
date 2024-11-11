package com.mitkov.weatherapp.WeatherApp.exceptions;

public class SensorNotCreatedException extends RuntimeException {
    public SensorNotCreatedException(String errorMsg) {
        super(errorMsg);
    }

}
