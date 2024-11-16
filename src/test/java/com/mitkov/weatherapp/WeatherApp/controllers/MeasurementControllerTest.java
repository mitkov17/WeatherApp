package com.mitkov.weatherapp.WeatherApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.services.MeasurementService;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import com.mitkov.weatherapp.WeatherApp.util.SortParameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private SensorService sensorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addMeasurementsTest() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurementDTO.setMeasurementValue(20.0);
        measurementDTO.setRaining(true);
        measurementDTO.setSensorId(sensor.getId());

        when(sensorService.findById(anyLong())).thenReturn(sensor);
        doNothing().when(measurementService).addMeasurement(any(Measurement.class));

        mockMvc.perform(post("/api/measurements/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMeasurementsTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getAllMeasurements()).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getRainyDaysTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getRainyDays()).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/rainyDays")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void filterMeasurementsTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.filterMeasurements(any(MeasurementUnit.class), anyDouble(), anyDouble())).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/filter")
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("min", "10.0")
                        .param("max", "20.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void sortMeasurementsTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.sortMeasurements(MeasurementUnit.DEGREES_CELSIUS, true, SortParameter.MEASUREMENT_VALUE)).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/sort")
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("ascending", "true")
                        .param("sortParam", "MEASUREMENT_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getMeasurementStatisticsTest() throws Exception {
        MeasurementsStatisticsDTO measurementsStatisticsDTO = new MeasurementsStatisticsDTO();
        measurementsStatisticsDTO.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurementsStatisticsDTO.setAvgValue(5.0);
        measurementsStatisticsDTO.setMinValue(1.0);
        measurementsStatisticsDTO.setMaxValue(10.0);
        measurementsStatisticsDTO.setCount(2L);

        when(measurementService.getMeasurementStatistics(any(MeasurementUnit.class), anyString(), anyString())).thenReturn(Collections.singletonList(measurementsStatisticsDTO));

        mockMvc.perform(get("/api/measurements/statistics")
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("startDate", "2024-11-16")
                        .param("endDate", "2024-11-17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].avgValue").value(5.0));
    }

    @Test
    void getMeasurementsByTimeRangeTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getMeasurementsByTimeRange(anyString(), anyString())).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/range")
                        .param("startDate", "2024-11-16")
                        .param("endDate", "2024-11-17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getPaginatedMeasurementsTest() throws Exception {
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        List<Measurement> measurements = Collections.singletonList(measurement);
        Page<Measurement> measurementPage = new PageImpl<>(measurements);

        when(measurementService.getPaginatedMeasurements(anyInt(), anyInt())).thenReturn(measurementPage);

        mockMvc.perform(get("/api/measurements/paginated")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].measurementValue").value(20.0));
    }
}
