package com.slava.repositories;

import com.slava.model.weatherApi.WAWeather;
import com.slava.model.weatherApi.WeatherAPI;
import com.slava.model.weatherApi.WACoordinates;
import com.slava.validators.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("WeatherAPIRepository")
public class WeatherAPIRepository implements IWeatherRepository<WAWeather>{
    private final WeatherAPI weatherAPI;
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherAPIRepository(WeatherAPI weatherAPI, RestTemplate restTemplate) {
        this.weatherAPI = weatherAPI;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<WAWeather> getWeather(String latitude, String longitude) {
        try {
            String urlencoded = UriComponentsBuilder
                    .fromHttpUrl(weatherAPI.getApiCurrentWeatherService())
                    .queryParam("key", weatherAPI.getAppId())
                    .queryParam("q", latitude + "," + longitude)
                    .encode()
                    .toUriString();

            WAWeather weather = restTemplate.getForObject(urlencoded, WAWeather.class);

            return Optional.ofNullable(weather);
        }catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new ServiceUnavailableException("Weather API is unavailable");
            }
            throw e;
        }
    }

    @Override
    public List<Optional<WAWeather>> searchWeather(String locationName) {
        String urlencoded = UriComponentsBuilder
                .fromHttpUrl(weatherAPI.getApiGeocoding())
                .queryParam("key", weatherAPI.getAppId())
                .queryParam("q", locationName)
                .encode()
                .toUriString();

        ResponseEntity<WACoordinates[]> geoResponse = restTemplate.getForEntity(urlencoded, WACoordinates[].class);
        List<WACoordinates> coordinates = List.of(geoResponse.getBody());
        List<Optional<WAWeather>> weathers =  coordinates.stream()
                .map(c -> getWeather(c.getLat().toString(), c.getLon().toString()))
                .collect(Collectors.toList());

        return weathers;
    }
}
