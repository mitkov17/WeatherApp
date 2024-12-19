package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.exceptions.UserNotFoundException;
import com.mitkov.weatherapp.WeatherApp.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
