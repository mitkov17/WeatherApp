package com.mitkov.weatherapp.WeatherApp.services;

import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.repositories.AppUserRepository;
import com.mitkov.weatherapp.WeatherApp.entities.Role;
import com.mitkov.weatherapp.WeatherApp.repositories.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final SensorRepository sensorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    @Transactional
    public void registerSensor(Sensor sensor) {
        sensor.setPassword(passwordEncoder.encode(sensor.getPassword()));
        sensorRepository.save(sensor);
    }

    @Transactional
    public void createDefaultAdminIfNotExists() {
        AppUser existingAdmin = appUserRepository.findByUsername("admin").orElse(null);

        if (existingAdmin == null) {
            existingAdmin = new AppUser();
            existingAdmin.setUsername("admin");
            existingAdmin.setPassword(passwordEncoder.encode("admin"));
            existingAdmin.setRole(Role.ROLE_ADMIN);

            appUserRepository.save(existingAdmin);
        }
    }
}
