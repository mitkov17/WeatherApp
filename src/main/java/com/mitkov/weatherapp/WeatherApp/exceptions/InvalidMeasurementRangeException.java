package com.mitkov.weatherapp.WeatherApp.exceptions;

public class InvalidMeasurementRangeException extends RuntimeException {

    public InvalidMeasurementRangeException(String message) {
        super(message);
    }

}
