package com.slava.model;

import lombok.Data;

@Data
public class OpenWeatherAPI {

    private static OpenWeatherAPI INSTANCE;
    private final String API_SERVICE = "";

    private final String APP_ID = "";

    private OpenWeatherAPI() {
    }

    public synchronized static OpenWeatherAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OpenWeatherAPI();
        }

        return INSTANCE;
    }
}
