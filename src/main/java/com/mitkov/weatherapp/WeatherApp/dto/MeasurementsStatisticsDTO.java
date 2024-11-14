package com.mitkov.weatherapp.WeatherApp.dto;

import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeasurementsStatisticsDTO {

    private MeasurementUnit measurementUnit;

    private Double avgValue;

    private Double minValue;

    private Double maxValue;

    private Long count;
}
