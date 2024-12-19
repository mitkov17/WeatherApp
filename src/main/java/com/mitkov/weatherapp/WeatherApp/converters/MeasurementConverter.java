package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementGetDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class MeasurementConverter {

    private final ModelMapper modelMapper;

    private final SensorService sensorService;

    private final SensorConverter sensorConverter;

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setId(null);
        measurement.setMeasuredAt(new Date());

        if (measurementDTO.getMeasurementValue() != null) {
            double roundedValue = BigDecimal.valueOf(measurementDTO.getMeasurementValue())
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            measurement.setMeasurementValue(roundedValue);
        }

        if (measurementDTO.getSensorId() != null) {
            Sensor sensor = sensorService.findById(measurementDTO.getSensorId());
            measurement.setSensor(sensor);
        } else {
            throw new SensorNotFoundException();
        }

        return measurement;
    }

    public MeasurementGetDTO convertToMeasurementGetDTO(Measurement measurement) {
        MeasurementGetDTO measurementGetDTO = modelMapper.map(measurement, MeasurementGetDTO.class);
        measurementGetDTO.setSensor(sensorConverter.convertToSensorDTO(measurement.getSensor()));
        return measurementGetDTO;
    }
}
