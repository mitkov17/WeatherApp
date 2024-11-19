package com.mitkov.weatherapp.WeatherApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitkov.weatherapp.WeatherApp.dto.SensorDTO;
import com.mitkov.weatherapp.WeatherApp.dto.SensorUserCreationDTO;
import com.mitkov.weatherapp.WeatherApp.entities.Measurement;
import com.mitkov.weatherapp.WeatherApp.entities.MeasurementUnit;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.InvalidSensorNameException;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorAlreadyExistsException;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.security.JWTUtil;
import com.mitkov.weatherapp.WeatherApp.services.AppUserDetailsService;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import com.mitkov.weatherapp.WeatherApp.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

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
    public void saveSensorTest() throws Exception {

        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("testName");

        SensorUserCreationDTO sensorUserCreationDTO = new SensorUserCreationDTO();

        sensorUserCreationDTO.setUsername("testUsername");
        sensorUserCreationDTO.setPassword("testPassword");
        sensorUserCreationDTO.setSensorDTO(sensorDTO);

        doNothing().when(sensorService).saveSensor(any(Sensor.class));

        mockMvc.perform(post("/api/sensors/register-sensor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorUserCreationDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllSensorsTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.getAllSensors()).thenReturn(Collections.singletonList(sensor));

        mockMvc.perform(get("/api/sensors")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    void getSensorByIdTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.findById(anyLong())).thenReturn(sensor);

        mockMvc.perform(get("/api/sensors/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testName"));
    }

    @Test
    void getMeasurementsBySensorTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

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
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].measurementValue").value(20.0));
    }

    @Test
    void searchForSensorTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setName("testName");

        when(sensorService.searchForSensor(anyString())).thenReturn(Collections.singletonList(sensor));

        mockMvc.perform(get("/api/sensors/search")
                        .header("Authorization", token)
                        .param("template", "testN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testName"));
    }

    @Test
    void deleteSensorTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_ADMIN);

        doNothing().when(sensorService).deleteSensor(anyLong());

        mockMvc.perform(delete("/api/sensors/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateSensorNameTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_ADMIN);

        doNothing().when(sensorService).updateSensorName(anyLong(), anyString());

        mockMvc.perform(patch("/api/sensors/1/update")
                        .header("Authorization", token)
                        .param("newName", "UpdatedName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getSensorByIdNotFoundTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_USER);

        when(sensorService.findById(anyLong())).thenThrow(new SensorNotFoundException(1L));

        mockMvc.perform(get("/api/sensors/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Sensor with id 1 does not exist!"));
    }

    @Test
    void saveSensorAlreadyExistsTest() throws Exception {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("testName");

        SensorUserCreationDTO sensorUserCreationDTO = new SensorUserCreationDTO();

        sensorUserCreationDTO.setUsername("testUsername");
        sensorUserCreationDTO.setPassword("testPassword");
        sensorUserCreationDTO.setSensorDTO(sensorDTO);

        doThrow(new SensorAlreadyExistsException(sensorDTO.getName())).when(sensorService).saveSensor(any(Sensor.class));

        mockMvc.perform(post("/api/sensors/register-sensor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorUserCreationDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Sensor \"testName\" already exists!"));
    }

    @Test
    void updateSensorNameInvalidTest() throws Exception {

        String token = "Bearer " + generateTestToken(Role.ROLE_ADMIN);

        doThrow(new InvalidSensorNameException("Name length should be between 2 and 100 symbols"))
                .when(sensorService).updateSensorName(anyLong(), anyString());

        mockMvc.perform(patch("/api/sensors/1/update")
                        .header("Authorization", token)
                        .param("newName", "A")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Name length should be between 2 and 100 symbols"));
    }

}
