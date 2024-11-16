package com.mitkov.weatherapp.WeatherApp.exceptions;

public class InvalidSensorNameException extends RuntimeException {

    public InvalidSensorNameException(String message) {
        super(message);
    }

}
