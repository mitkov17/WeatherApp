package com.mitkov.weatherapp.WeatherApp.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found!");
    }
}
