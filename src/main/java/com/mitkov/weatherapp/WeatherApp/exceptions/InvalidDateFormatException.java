package com.mitkov.weatherapp.WeatherApp.exceptions;

public class InvalidDateFormatException extends RuntimeException {

    public InvalidDateFormatException(String message) {
        super(message);
    }

}
