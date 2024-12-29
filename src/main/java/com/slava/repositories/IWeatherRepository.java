package com.slava.repositories;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public interface IWeatherRepository<W> {
    Optional<W> getWeather(String latitude, String longitude);
    List<Optional<W>> searchWeather(String locationName);
}
