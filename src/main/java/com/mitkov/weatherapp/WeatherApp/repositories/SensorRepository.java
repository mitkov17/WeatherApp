package com.mitkov.weatherapp.WeatherApp.repositories;

import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByNameContainingIgnoreCase(String template);
}
