package com.slava.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OpenWeatherAPI {

    @Value("${openweather.api.service}")
    private String apiService;

    @Value("${openweather.api.app-id}")
    private String appId;

    public String getAPP_ID() {
        return appId;
    }
    public String getAPI_SERVICE() {
        return apiService;
    }
}
