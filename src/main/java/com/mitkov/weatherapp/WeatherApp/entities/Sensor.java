package com.mitkov.weatherapp.WeatherApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mitkov.weatherapp.WeatherApp.util.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
    @JsonView(View.Summary.class)
    private Long id;

    @Column(name = "name")
    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 symbols")
    @JsonView(View.Summary.class)
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
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JsonIgnore
    private List<Measurement> measurements;

}
