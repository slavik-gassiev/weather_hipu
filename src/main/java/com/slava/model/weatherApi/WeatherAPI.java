package com.slava.model.weatherApi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class WeatherAPI {
    @Value("${weatherapi.base-url}")
    private String apiCurrentWeatherService;
    @Value("${weatherapi.search-url}")
    private String apiGeocoding;

    @Value("${weatherapi.key}")
    private String appId;
}
