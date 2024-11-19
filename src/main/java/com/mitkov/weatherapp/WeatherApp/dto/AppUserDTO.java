package com.mitkov.weatherapp.WeatherApp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 symbols")
    private String username;

    private String password;

}
