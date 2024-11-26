package com.slava.services;

import com.slava.dao.IWeatherDao;
import com.slava.dao.WeatherDao;
import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    IWeatherDao weatherDao = new WeatherDao();

    public List<Weather> getWeathersByLocations(List<Location> locations) {

        return (List<Weather>) locations.stream()
                .map(location -> weatherDao.getWeather(location))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}
