package com.mitkov.weatherapp.WeatherApp.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location {

    private Double latitude;

    private Double longitude;
}
