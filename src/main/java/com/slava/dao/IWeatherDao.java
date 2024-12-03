package com.slava.dao;

import java.util.Optional;

public interface IWeatherDao<W, L> {
    Optional<W> getWeather(L location);
    Optional<W> searchWeather(L location);
}
