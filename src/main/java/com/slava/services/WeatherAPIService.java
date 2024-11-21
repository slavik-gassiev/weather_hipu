package com.slava.services;

import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherAPIService {

    private static final String APP_ID = "24f20976ca9c63a1038e8a5a72197f92";

    public List<Weather> getWeathersByLocations(List<Location> locations) {
        return null;
    }
}
