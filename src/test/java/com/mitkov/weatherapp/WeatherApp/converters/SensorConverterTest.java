package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SensorConverterTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SensorConverter sensorConverter;

    @Test
    public void testConvertToSensor() {

        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("Test Sensor");

        Sensor mockSensor = new Sensor();
        mockSensor.setName("Test Sensor");

        when(modelMapper.map(sensorDTO, Sensor.class)).thenReturn(mockSensor);

        Sensor sensor = sensorConverter.convertToSensor(sensorDTO);

        assertNotNull(sensor);
        assertEquals("Test Sensor", sensor.getName());
        assertNotNull(sensor.getDateOfInstallation());
    }
}
