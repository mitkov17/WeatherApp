package com.mitkov.weatherapp.WeatherApp.repositories;

import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
