package com.slava.services;

import com.slava.model.imodel.IWeather;
import com.slava.repositories.IWeatherRepository;
import com.slava.repositories.WeatherRepository;
import com.slava.entities.Location;
import com.slava.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<String, IWeatherRepository<? extends IWeather>> repos;
    private IWeatherRepository<? extends IWeather> currentRepo;

    @Autowired
    public WeatherService(Map<String, IWeatherRepository<? extends IWeather>> repos) {
        this.repos = repos;
        this.currentRepo = repos.get("WeatherRepository");   // дефолт
    }

    public void switchRepository(String beanName) {
        IWeatherRepository<? extends IWeather> repo = repos.get(beanName);
        if (repo == null) {
            throw new IllegalArgumentException("Нет такого источника: " + beanName);
        }
        this.currentRepo = repo;
    }

    public Optional<? extends IWeather> getWeather(Location loc) {
        return currentRepo.getWeather(
                loc.getLatitude().toString(),
                loc.getLongitude().toString());
    }

    public List<? extends IWeather> searchWeather(String name) {
        return currentRepo.searchWeather(name)
                .stream()
                .flatMap(Optional::stream)
                .toList();
    }

    public List<? extends IWeather> getWeathersByLocations(List<Location> locs) {
        return locs.stream()
                .map(l -> currentRepo.getWeather(
                        l.getLatitude().toString(),
                        l.getLongitude().toString()))
                .flatMap(Optional::stream)
                .toList();
    }
}

