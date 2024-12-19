package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.repositories.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @InjectMocks
    private SensorService sensorService;

    @Mock
    private SensorRepository sensorRepository;

//    @Test
//    public void saveSensorTest() {
//
//        Sensor sensor = new Sensor();
//        sensor.setName("testName");
//
//        sensorService.saveSensor(sensor);
//
//        verify(sensorRepository, times(1)).save(sensor);
//    }
//
//    @Test
//    public void getSensorsTest() {
//        Sensor sensor = new Sensor();
//        sensor.setName("testName");
//
//        when(sensorRepository.findAll()).thenReturn(Collections.singletonList(sensor));
//
//        List<Sensor> list = sensorService.getAllSensors();
//
//        assertNotNull(list);
//        assertEquals(1, list.size());
//        assertEquals("testName", list.get(0).getName());
//    }
//
//    @Test
//    public void findByIdTest() {
//        Sensor sensor = new Sensor();
//        sensor.setId(1L);
//        sensor.setName("testName");
//
//        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
//
//        Sensor result = sensorService.findById(1L);
//
//        assertNotNull(result);
//        assertEquals("testName", result.getName());
//    }
//
//    @Test
//    public void getMeasurementsBySensorTest() {
//        Sensor sensor = new Sensor();
//        sensor.setId(1L);
//        sensor.setName("testName");
//
//        Measurement measurement = new Measurement();
//        measurement.setMeasurementValue(20.0);
//        measurement.setSensor(sensor);
//
//        sensor.setMeasurements(Collections.singletonList(measurement));
//
//        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
//
//        List<Measurement> result = sensorService.getMeasurementsBySensor(1L);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(20.0, result.get(0).getMeasurementValue());
//    }
//
//    @Test
//    public void searchForSensorTest() {
//        Sensor sensor = new Sensor();
//        sensor.setId(1L);
//        sensor.setName("testName");
//
//        when(sensorRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Collections.singletonList(sensor));
//
//        List<Sensor> result = sensorService.searchForSensor("testN");
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("testName", result.get(0).getName());
//    }
//
//    @Test
//    public void deleteSensorTest() {
//        Sensor sensor = new Sensor();
//        sensor.setId(1L);
//        sensor.setName("testName");
//
//        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
//        doNothing().when(sensorRepository).delete(sensor);
//
//        sensorService.deleteSensor(1L);
//
//        verify(sensorRepository, times(1)).delete(sensor);
//    }
//
//    @Test
//    public void deleteSensorNotFoundTest() {
//        when(sensorRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(SensorNotFoundException.class, () -> sensorService.deleteSensor(1L));
//    }
//
//    @Test
//    public void updateSensorNameTest() {
//
//        Sensor sensor = new Sensor();
//        sensor.setId(1L);
//        sensor.setName("testName");
//
//        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
//
//        sensorService.updateSensorName(1L, "secondTestName");
//
//        assertEquals("secondTestName", sensor.getName());
//    }

}
