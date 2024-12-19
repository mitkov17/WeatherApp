package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeasurementConverterTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private MeasurementConverter measurementConverter;

//    @Test
//    public void testConvertToMeasurement() {
//        MeasurementDTO measurementDTO = new MeasurementDTO();
//        measurementDTO.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
//        measurementDTO.setMeasurementValue(25.0);
//        measurementDTO.setSensorId(1L);
//
//        Sensor mockSensor = new Sensor();
//        mockSensor.setId(1L);
//        mockSensor.setName("Test Sensor");
//
//        when(sensorService.findById(1L)).thenReturn(mockSensor);
//        when(modelMapper.map(measurementDTO, Measurement.class)).thenAnswer(invocation -> {
//            Measurement measurement = new Measurement();
//            measurement.setMeasurementUnit(measurementDTO.getMeasurementUnit());
//            measurement.setMeasurementValue(measurementDTO.getMeasurementValue());
//            return measurement;
//        });
//
//        Measurement measurement = measurementConverter.convertToMeasurement(measurementDTO);
//
//        assertNotNull(measurement);
//        assertNull(measurement.getId());
//        assertNotNull(measurement.getMeasuredAt());
//        assertEquals(MeasurementUnit.DEGREES_CELSIUS, measurement.getMeasurementUnit());
//        assertEquals(25.0, measurement.getMeasurementValue());
//        assertEquals(mockSensor, measurement.getSensor());
//    }
}
