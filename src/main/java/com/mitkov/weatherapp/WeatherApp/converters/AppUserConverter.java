package com.mitkov.weatherapp.WeatherApp.converters;

import com.mitkov.weatherapp.WeatherApp.dto.RegistrationUserDTO;
import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserConverter {

    private final ModelMapper modelMapper;

    public AppUser convertToAppUser(RegistrationUserDTO registrationUserDTO) {
        AppUser appUser = modelMapper.map(registrationUserDTO, AppUser.class);
        appUser.setRole(Role.ROLE_USER);
        return appUser;
    }
}
