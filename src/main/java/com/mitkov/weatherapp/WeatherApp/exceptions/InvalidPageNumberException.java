package com.mitkov.weatherapp.WeatherApp.exceptions;

public class InvalidPageNumberException extends RuntimeException {

    public InvalidPageNumberException(String message) {
        super(message);
    }
}
