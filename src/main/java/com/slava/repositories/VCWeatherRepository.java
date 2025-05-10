package com.slava.repositories;

import com.slava.model.crossing_weather.VCWeather;
import com.slava.model.crossing_weather.VCWeatherAPI;
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

@Repository("VCWeatherRepository")
public class VCWeatherRepository implements IWeatherRepository<VCWeather> {

    private final VCWeatherAPI weatherAPI;
    private final RestTemplate restTemplate;

    @Autowired
    public VCWeatherRepository(VCWeatherAPI weatherAPI, RestTemplate restTemplate) {
        this.weatherAPI = weatherAPI;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<VCWeather> getWeather(String latitude, String longitude) {
        try {
            // Visual Crossing принимает координаты как "lat,lon"
            String location = latitude + "," + longitude;

            String urlencoded = UriComponentsBuilder
                    .fromHttpUrl(weatherAPI.getBaseUrl() + "/" + location)
                    .queryParam("key", weatherAPI.getKey())
                    .queryParam("unitGroup", weatherAPI.getUnitGroup())
                    .queryParam("include", weatherAPI.getInclude())
                    .queryParam("lang", weatherAPI.getLang())
                    .queryParam("contentType", "json")
                    .encode()
                    .toUriString();

            VCWeather weather = restTemplate.getForObject(urlencoded, VCWeather.class);

            if (weather != null && (weather.getName() == null || weather.getName().isBlank())) {
                // хоть что-то показываем пользователю
                weather.setName(location);
                weather.getCoordinates().setName(location);
            }
            return Optional.ofNullable(weather);

        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new ServiceUnavailableException("Visual Crossing API is unavailable");
            }
            throw e;
        }
    }

    @Override
    public List<Optional<VCWeather>> searchWeather(String locationName) {
        try {
            String urlencoded = UriComponentsBuilder
                    .fromHttpUrl(weatherAPI.getBaseUrl() + "/" + locationName)
                    .queryParam("key", weatherAPI.getKey())
                    .queryParam("unitGroup", weatherAPI.getUnitGroup())
                    .queryParam("include", weatherAPI.getInclude())
                    .queryParam("lang", weatherAPI.getLang())
                    .queryParam("contentType", "json")
                    .encode()
                    .toUriString();

            VCWeather weather = restTemplate.getForObject(urlencoded, VCWeather.class);

            if (weather != null && (weather.getName() == null || weather.getName().isBlank())) {
                weather.setName(locationName);
                weather.getCoordinates().setName(locationName);
            }


            return List.of(Optional.ofNullable(weather));
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new ServiceUnavailableException("Visual Crossing API is unavailable");
            }
            throw e;
        }
    }
}
