package com.slava.dao;

import com.slava.entities.Location;
import com.slava.model.OpenWeatherAPI;
import com.slava.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Repository
public class WeatherDao implements IWeatherDao<Weather, Location> {

    private final OpenWeatherAPI openWeatherAPI;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherDao(OpenWeatherAPI openWeatherAPI, RestTemplate restTemplate) {
        this.openWeatherAPI = openWeatherAPI;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Weather> getWeather(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        String urlencoded = UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getAPI_SERVICE())
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", openWeatherAPI.getAPP_ID())
                .queryParam("units", "metric")
                .encode()
                .toUriString();

        return fetchWeather(urlencoded);
    }

    @Override
    public Optional<Weather> searchWeather(Location location) {
        String locationName = location.getName();

        String urlencoded = UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getAPI_SERVICE())
                .queryParam("q", locationName)
                .queryParam("appid", openWeatherAPI.getAPP_ID())
                .queryParam("units", "metric")
                .encode()
                .toUriString();

        return fetchWeather(urlencoded);
    }

    private Optional<Weather> fetchWeather(String url) {
        try {
            Weather weather = restTemplate.getForObject(url, Weather.class);
            return Optional.ofNullable(weather);
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
            return Optional.empty();
        }
    }
}
