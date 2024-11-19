package com.mitkov.weatherapp.WeatherApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorUserCreationDTO {

    private String username;

    private String password;

    private SensorDTO sensorDTO;
}
