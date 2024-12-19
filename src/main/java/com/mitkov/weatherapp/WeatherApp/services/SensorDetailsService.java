package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.exceptions.SensorNotFoundException;
import com.mitkov.weatherapp.WeatherApp.repositories.SensorRepository;
import com.mitkov.weatherapp.WeatherApp.security.SensorDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorDetailsService implements UserDetailsService {

    private final SensorRepository sensorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws SensorNotFoundException {
        Sensor sensor = sensorRepository.findByName(username).orElseThrow(SensorNotFoundException::new);

        return new SensorDetails(sensor);
    }
}
