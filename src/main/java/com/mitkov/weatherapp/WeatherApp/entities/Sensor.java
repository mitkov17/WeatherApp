package com.mitkov.weatherapp.WeatherApp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sensor")
@Getter
@Setter
public class Sensor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(min = 1, max = 100, message = "Name length should be between 1 and 100 symbols")
    private String name;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type")
    private SensorType sensorType;

    @Column(name = "date_of_installation")
    @Temporal(TemporalType.DATE)
    private Date dateOfInstallation;

    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements;

}
