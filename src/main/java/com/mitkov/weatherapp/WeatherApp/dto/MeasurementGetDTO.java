package com.mitkov.weatherapp.WeatherApp.dto;

import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MeasurementGetDTO {

    @NotNull(message = "Raining index should not be null")
    private Boolean raining;

    @NotNull(message = "Measurement unit should not be null")
    private MeasurementUnit measurementUnit;

    @NotNull(message = "Measurement value should not be null!")
    private Double measurementValue;

    private Date measuredAt;

    private SensorDTO sensor;

}
