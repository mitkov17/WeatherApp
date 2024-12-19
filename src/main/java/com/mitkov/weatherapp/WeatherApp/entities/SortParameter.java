package com.mitkov.weatherapp.WeatherApp.entities;

import lombok.Getter;

@Getter
public enum SortParameter {
    MEASUREMENT_VALUE("measurementValue"),
    MEASUREMENT_TIME("measuredAt");

    private final String sortField;

    SortParameter(String sortField) {
        this.sortField = sortField;
    }
}

