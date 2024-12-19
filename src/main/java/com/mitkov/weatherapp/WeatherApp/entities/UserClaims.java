package com.mitkov.weatherapp.WeatherApp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserClaims {

    private String username;

    private String role;
}
