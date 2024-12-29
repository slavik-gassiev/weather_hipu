package com.slava.services;

import com.slava.repositories.WeatherRepository;
import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Optional<Weather> getWeather(Location location) {
        return weatherRepository.getWeather(location.getLatitude().toString(), location.getLongitude().toString());
    }

    public List<Weather> searchWeather(String locationName) {
        return weatherRepository.searchWeather(locationName)
                .stream().flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    public List<Weather> getWeathersByLocations(List<Location> locations) {

        return (List<Weather>) locations.stream()
                .map(location -> weatherRepository.getWeather(location.getLatitude().toString(), location.getLongitude().toString()))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}

