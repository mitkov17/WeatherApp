package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.SensorUserCreationDTO;
import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class AppUserConverter {

    public AppUser convertSensorToAppUser(SensorUserCreationDTO sensorUserCreationDTO) {

        AppUser appUser = new AppUser();
        appUser.setUsername(sensorUserCreationDTO.getUsername());
        appUser.setPassword(sensorUserCreationDTO.getPassword());
        appUser.setRole(Role.ROLE_SENSOR);

        return appUser;
    }
}
