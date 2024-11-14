package com.mitkov.weatherapp.WeatherApp.entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.mitkov.weatherapp.WeatherApp.util.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "measurement")
@Getter
@Setter
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(name = "raining")
    @NotNull(message = "Raining index should not be null")
    @JsonView(View.Summary.class)
    private Boolean raining;

    @Enumerated(EnumType.STRING)
    @Column(name = "measurement_unit")
    @NotNull(message = "Measurement unit should not be null")
    @JsonView(View.Summary.class)
    private MeasurementUnit measurementUnit;

    @Column(name = "measurement_value")
    @NotNull(message = "Measurement value should not be null!")
    @JsonView(View.Summary.class)
    private Double measurementValue;

    @Column(name = "measured_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "Time of measuring should not be null")
    @JsonView(View.Summary.class)
    private Date measuredAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @JsonView(View.Summary.class)
    private Sensor sensor;
}
