package com.slava.dao;

import java.util.List;
import java.util.Optional;

public interface IWeatherDao<W> {
    Optional<W> getWeather(String latitude, String longitude);
    List<Optional<W>> searchWeather(String locationName);
}
