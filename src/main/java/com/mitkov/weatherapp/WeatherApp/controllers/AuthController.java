package com.mitkov.weatherapp.WeatherApp.controllers;

import com.mitkov.weatherapp.WeatherApp.converters.AppUserConverter;
import com.mitkov.weatherapp.WeatherApp.converters.SensorConverter;
import com.mitkov.weatherapp.WeatherApp.dto.AuthenticationDTO;
import com.mitkov.weatherapp.WeatherApp.dto.RegistrationSensorDTO;
import com.mitkov.weatherapp.WeatherApp.dto.RegistrationUserDTO;
import com.mitkov.weatherapp.WeatherApp.entities.AppUser;
import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import com.mitkov.weatherapp.WeatherApp.security.JWTUtil;
import com.mitkov.weatherapp.WeatherApp.services.AppUserService;
import com.mitkov.weatherapp.WeatherApp.services.RegistrationService;
import com.mitkov.weatherapp.WeatherApp.services.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final SensorConverter sensorConverter;

    private final AppUserService appUserService;

    private final SensorService sensorService;

    private final AppUserConverter appUserConverter;

    @PostMapping("/registration")
    public ResponseEntity<String> performRegistration(@RequestBody @Valid RegistrationUserDTO registrationUserDTO) {

        AppUser appUser = appUserConverter.convertToAppUser(registrationUserDTO);

        registrationService.register(appUser);

        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/register-sensor")
    public ResponseEntity<String> performSensorRegistration(@RequestBody @Valid RegistrationSensorDTO registrationSensorDTO) {
        Sensor sensor = sensorConverter.convertToSensor(registrationSensorDTO);

        registrationService.registerSensor(sensor);

        return ResponseEntity.ok("Sensor has been registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        AppUser appUser = appUserService.findByUsername(authenticationDTO.getUsername());

        String token = jwtUtil.generateToken(appUser.getUsername(), appUser.getRole());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/login-sensor")
    public Map<String, String> performSensorLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        Sensor sensor = sensorService.findByName(authenticationDTO.getUsername());

        String token = jwtUtil.generateToken(sensor.getName(), sensor.getRole());
        return Map.of("jwt-token", token);
    }
}
