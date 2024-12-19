package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.RegistrationSensorDTO;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class SensorConverter {

    private final ModelMapper modelMapper;

    public Sensor convertToSensor(RegistrationSensorDTO registrationSensorDTO) {
        Sensor sensor = modelMapper.map(registrationSensorDTO, Sensor.class);
        sensor.setDateOfInstallation(new Date());
        sensor.setRole(Role.ROLE_SENSOR);

        return sensor;
    }

    public SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
