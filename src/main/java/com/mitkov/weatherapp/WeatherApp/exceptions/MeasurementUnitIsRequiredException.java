package com.mitkov.weatherapp.WeatherApp.exceptions;

public class MeasurementUnitIsRequiredException extends RuntimeException {

    public MeasurementUnitIsRequiredException(String message) {
        super(message);
    }

}
