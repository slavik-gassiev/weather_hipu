package com.slava.dao;

import com.slava.entities.Location;
import com.slava.model.OpenWeatherAPI;
import com.slava.model.Weather;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public class WeatherDao implements IWeatherDao<Weather, Location>{

    OpenWeatherAPI openWeatherAPI = OpenWeatherAPI.getInstance();

    @Override
    public Optional<Weather> getWeather(Location location) {
        RestTemplate restTemplate = new RestTemplate();
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        String urlencoded = UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getAPI_SERVICE())
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", openWeatherAPI.getAPP_ID())
                .queryParam("units", "metric")
                .encode()
                .toUriString();

        URI uri = URI.create(urlencoded);

        Weather weather = null;
        try {
            weather = restTemplate.getForObject(uri, Weather.class);
            return Optional.ofNullable(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
