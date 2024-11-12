package com.mitkov.weatherapp.WeatherApp.dto;

import com.mitkov.weatherapp.WeatherApp.entities.Location;
import com.mitkov.weatherapp.WeatherApp.entities.SensorType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDTO {

    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 symbols")
    private String name;

    private Location location;

    private SensorType sensorType;

}
