package com.mitkov.weatherapp.WeatherApp.exceptions;

public class InvalidPageSizeException extends RuntimeException {

    public InvalidPageSizeException(String message) {
        super(message);
    }
}
