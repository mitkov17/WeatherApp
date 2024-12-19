package com.mitkov.weatherapp.WeatherApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementDTO;
import com.mitkov.weatherapp.WeatherApp.dto.MeasurementsStatisticsDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.InvalidDateFormatException;
import com.mitkov.weatherapp.WeatherApp.exceptions.InvalidMeasurementRangeException;
import com.mitkov.weatherapp.WeatherApp.security.JWTUtil;
import com.mitkov.weatherapp.WeatherApp.services.AppUserDetailsService;
import com.mitkov.weatherapp.WeatherApp.services.MeasurementService;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import com.mitkov.weatherapp.WeatherApp.entities.SortParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private SensorService sensorService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @BeforeEach
    public void setup() {
        UserDetails userDetails = new User("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(appUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
    }

    private String generateTestToken(Role role) {
        return jwtUtil.generateToken("testUser", role);
    }

    @Test
    public void addMeasurementsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_SENSOR);

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
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllMeasurementsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getAllMeasurements()).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getRainyDaysTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getRainyDays()).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/rainyDays")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void filterMeasurementsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.filterMeasurements(any(MeasurementUnit.class), anyDouble(), anyDouble())).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/filter")
                        .header("Authorization", token)
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("min", "10.0")
                        .param("max", "20.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void sortMeasurementsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.sortMeasurements(MeasurementUnit.DEGREES_CELSIUS, true, SortParameter.MEASUREMENT_VALUE)).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/sort")
                        .header("Authorization", token)
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("ascending", "true")
                        .param("sortParam", "MEASUREMENT_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getMeasurementStatisticsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        MeasurementsStatisticsDTO measurementsStatisticsDTO = new MeasurementsStatisticsDTO();
        measurementsStatisticsDTO.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurementsStatisticsDTO.setAvgValue(5.0);
        measurementsStatisticsDTO.setMinValue(1.0);
        measurementsStatisticsDTO.setMaxValue(10.0);
        measurementsStatisticsDTO.setCount(2L);

        when(measurementService.getMeasurementStatistics(any(MeasurementUnit.class), anyString(), anyString())).thenReturn(Collections.singletonList(measurementsStatisticsDTO));

        mockMvc.perform(get("/api/measurements/statistics")
                        .header("Authorization", token)
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("startDate", "2024-11-16")
                        .param("endDate", "2024-11-17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].avgValue").value(5.0));
    }

    @Test
    void getMeasurementsByTimeRangeTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        when(measurementService.getMeasurementsByTimeRange(anyString(), anyString())).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/api/measurements/range")
                        .header("Authorization", token)
                        .param("startDate", "2024-11-16")
                        .param("endDate", "2024-11-17")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void getPaginatedMeasurementsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurement.setMeasurementValue(20.0);

        List<Measurement> measurements = Collections.singletonList(measurement);
        Page<Measurement> measurementPage = new PageImpl<>(measurements);

        when(measurementService.getPaginatedMeasurements(anyInt(), anyInt())).thenReturn(measurementPage);

        mockMvc.perform(get("/api/measurements/paginated")
                        .header("Authorization", token)
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].measurementValue").value(20.0));
    }

    @Test
    void addMeasurementInvalidDateFormatTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_SENSOR);

        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setMeasurementUnit(MeasurementUnit.DEGREES_CELSIUS);
        measurementDTO.setMeasurementValue(20.0);
        measurementDTO.setSensorId(1L);
        measurementDTO.setRaining(true);

        doThrow(new InvalidDateFormatException("Invalid date format. Please use 'yyyy/MM/dd'"))
                .when(measurementService).addMeasurement(any(Measurement.class));

        mockMvc.perform(post("/api/measurements/add")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid date format. Please use 'yyyy/MM/dd'"));
    }

    @Test
    void filterMeasurementsInvalidRangeTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        when(measurementService.filterMeasurements(any(MeasurementUnit.class), anyDouble(), anyDouble()))
                .thenThrow(new InvalidMeasurementRangeException("Min value cannot be greater than Max value"));

        mockMvc.perform(get("/api/measurements/filter")
                        .header("Authorization", token)
                        .param("measurementUnit", "DEGREES_CELSIUS")
                        .param("min", "20.0")
                        .param("max", "10.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Min value cannot be greater than Max value"));
    }

    @Test
    void getMeasurementsByTimeRangeInvalidDateTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        when(measurementService.getMeasurementsByTimeRange(anyString(), anyString()))
                .thenThrow(new InvalidDateFormatException("Invalid date format. Please use 'yyyy/MM/dd'"));

        mockMvc.perform(get("/api/measurements/range")
                        .header("Authorization", token)
                        .param("startDate", "16-11-2024")
                        .param("endDate", "17-11-2024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid date format. Please use 'yyyy/MM/dd'"));
    }

}
