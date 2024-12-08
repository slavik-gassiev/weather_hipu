package com.slava.services;

import com.slava.dao.IWeatherDao;
import com.slava.dao.WeatherDao;
import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Optional<Weather> getWeather(Location location) {
        return weatherDao.getWeather(location.getLatitude().toString(), location.getLongitude().toString());
    }

    public List<Weather> searchWeather(String locationName) {
        return weatherDao.searchWeather(locationName)
                .stream().flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    public List<Weather> getWeathersByLocations(List<Location> locations) {

        return (List<Weather>) locations.stream()
                .map(location -> weatherDao.getWeather(location.getLatitude().toString(), location.getLongitude().toString()))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}

