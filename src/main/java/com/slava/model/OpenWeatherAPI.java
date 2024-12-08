package com.slava.model;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OpenWeatherAPI {
    @Value("${openweather.cw.api.service}")
    private String apiCurrentWeatherService;
    @Value("${openweather.geocoding.api.service}")
    private String apiGeocoding;

    @Value("${openweather.api.app-id}")
    private String appId;

    public String getAPP_ID() {
        return appId;
    }
    public String getApiCurrentWeatherService() {
        return apiCurrentWeatherService;
    }

    public String getApiGeocoding() {
        return apiGeocoding;
    }
}
