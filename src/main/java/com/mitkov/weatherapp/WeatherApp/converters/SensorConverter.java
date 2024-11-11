package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SensorConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public SensorConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Sensor convertToSensor(SensorDTO sensorDTO) {
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        sensor.setDateOfInstallation(new Date());

        return sensor;
    }
}
