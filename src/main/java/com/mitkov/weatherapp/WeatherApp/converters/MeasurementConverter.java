package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class MeasurementConverter {

    private final ModelMapper modelMapper;

    private final SensorService sensorService;

    public Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setId(null);
        measurement.setMeasuredAt(new Date());

        if (measurementDTO.getSensorId() != null) {
            Sensor sensor = sensorService.findById(measurementDTO.getSensorId());
            measurement.setSensor(sensor);
        } else {
            throw new SensorNotFoundException();
        }

        return measurement;
    }
}
