package com.mitkov.weatherapp.WeatherApp.dto;

import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementDTO {

    @NotNull(message = "Raining index should not be null")
    private Boolean raining;

    @NotNull(message = "Measurement unit should not be null")
    private MeasurementUnit measurementUnit;

    @NotNull(message = "Measurement value should not be null!")
    private Double measurementValue;

    @NotNull(message = "Sensor ID should not be null")
    private Long sensorId;
}
