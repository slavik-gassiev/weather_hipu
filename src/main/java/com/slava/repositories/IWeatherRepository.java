package com.slava.repositories;

import com.slava.model.imodel.IWeather;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public interface IWeatherRepository<T extends IWeather> {

    Optional<T> getWeather(String lat, String lon);

    List<Optional<T>> searchWeather(String locationName);
}

