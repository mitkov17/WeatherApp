package com.mitkov.weatherapp.WeatherApp.repositories;

import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findByRainingTrue();

    List<Measurement> findByMeasurementUnit(MeasurementUnit type);

    List<Measurement> findByMeasurementUnit(MeasurementUnit unit, Sort sort);

    List<Measurement> findByMeasurementUnitAndMeasurementValueBetween(MeasurementUnit unit, Double min, Double max);

    List<Measurement> findByMeasurementUnitAndMeasurementValueBefore(MeasurementUnit unit, Double value);

    List<Measurement> findByMeasurementUnitAndMeasurementValueAfter(MeasurementUnit unit, Double value);

    List<Measurement> findByMeasurementUnitAndMeasuredAtBetween(MeasurementUnit unit, Date startDate, Date endDate);

    List<Measurement> findByMeasurementUnitAndMeasuredAtAfter(MeasurementUnit unit, Date startDate);

    List<Measurement> findByMeasurementUnitAndMeasuredAtBefore(MeasurementUnit unit, Date endDate);

    List<Measurement> findByMeasuredAtBetween(Date startDate, Date endDate);
}
