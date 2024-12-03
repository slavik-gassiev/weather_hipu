package com.slava.services;

import com.slava.dao.IWeatherDao;
import com.slava.dao.WeatherDao;
import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final WeatherDao weatherDao;

    @Autowired
    public WeatherService(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public Weather getWeather(Location location) {
        return weatherDao.getWeather(location)
                .orElseThrow(() -> new RuntimeException("Weather not found for location: " + location.getName()));
    }

    public Weather searchWeather(String locationName) {
        Location location = new Location();
        location.setName(locationName);

        return weatherDao.searchWeather(location)
                .orElseThrow(() -> new RuntimeException("Weather not found for location name: " + locationName));
    }
}

