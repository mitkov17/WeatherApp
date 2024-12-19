package com.mitkov.weatherapp.WeatherApp.security;

import com.mitkov.weatherapp.WeatherApp.entities.Sensor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public class SensorDetails implements UserDetails {

    private final Sensor sensor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_SENSOR"));
    }

    @Override
    public String getPassword() {
        return this.sensor.getPassword();
    }

    @Override
    public String getUsername() {
        return this.sensor.getName();
    }
}
