package com.mitkov.weatherapp.WeatherApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorService sensorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveSensorTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        doNothing().when(sensorService).saveSensor(sensor);

        mockMvc.perform(post("/api/sensors/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensor)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllSensorsTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.getAllSensors()).thenReturn(Collections.singletonList(sensor));

        mockMvc.perform(get("/api/sensors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    void getSensorByIdTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.findById(anyLong())).thenReturn(sensor);

        mockMvc.perform(get("/api/sensors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    void getMeasurementsBySensorTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);
        measurement.setSensor(sensor);

        when(sensorService.getMeasurementsBySensor(anyLong())).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/sensors/1/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void searchForSensorTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.searchForSensor(anyString())).thenReturn(Collections.singletonList(sensor));

        mockMvc.perform(get("/api/sensors/search")
                        .param("template", "testN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    void deleteSensorTest() throws Exception {
        doNothing().when(sensorService).deleteSensor(anyLong());

        mockMvc.perform(delete("/api/sensors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateSensorNameTest() throws Exception {
        doNothing().when(sensorService).updateSensorName(anyLong(), anyString());

        mockMvc.perform(patch("/api/sensors/1/update")
                        .param("newName", "UpdatedName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
