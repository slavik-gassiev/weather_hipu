package com.slava.services;

import com.slava.config.TestConfig;
import com.slava.entities.Location;
import com.slava.model.OpenWeatherAPI;
import com.slava.model.Weather;
import com.slava.validators.ServiceUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private OpenWeatherAPI openWeatherAPI;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getWeather_returnsWeatherForLocation() {
        String mockResponse = """
            {
                "name": "Moscow",
                "coord": { "lat": 55.75, "lon": 37.62 },
                "main": { "temp": 25.5, "humidity": 60 },
                "weather": [{ "main": "Clear", "description": "clear sky", "icon": "01d" }],
                "wind": { "speed": 5.0 }
            }
        """;

        mockServer.expect(requestTo(
                        UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getApiCurrentWeatherService())
                                .queryParam("lat", "55.75")
                                .queryParam("lon", "37.62")
                                .queryParam("appid", openWeatherAPI.getAppId())
                                .queryParam("units", "metric")
                                .queryParam("lang", "ru")
                                .encode()
                                .toUriString()))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mockResponse));

        Location location = new Location(55.75, 37.62);
        Optional<Weather> weather = weatherService.getWeather(location);

        assertTrue(weather.isPresent());
        assertEquals("Moscow", weather.get().getName());
        assertEquals("Clear", weather.get().getWeatherDetails().get(0).getMain());
        assertEquals("clear sky", weather.get().getWeatherDetails().get(0).getDescription());
        assertEquals("25.5", weather.get().getParameters().getTemp());
        assertEquals("60", weather.get().getParameters().getHumidity());
        assertEquals("5.0", weather.get().getWind().getSpeed());
    }

    @Test
    void searchWeather_returnsListOfWeatherObjects() {
        String geoResponse = """
            [
                { "name": "Moscow", "lat": 55.75, "lon": 37.62 },
                { "name": "Saint Petersburg", "lat": 59.93, "lon": 30.34 }
            ]
        """;

        String moscowWeatherResponse = """
            {
                "name": "Moscow",
                "coord": { "lat": 55.75, "lon": 37.62 },
                "main": { "temp": 25.5, "humidity": 60 },
                "weather": [{ "main": "Clear", "description": "clear sky", "icon": "01d" }],
                "wind": { "speed": 5.0 }
            }
        """;

        String spbWeatherResponse = """
            {
                "name": "Saint Petersburg",
                "coord": { "lat": 59.93, "lon": 30.34 },
                "main": { "temp": 20.0, "humidity": 70 },
                "weather": [{ "main": "Clouds", "description": "few clouds", "icon": "02d" }],
                "wind": { "speed": 3.0 }
            }
        """;

        mockServer.expect(requestTo(
                        UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getApiGeocoding())
                                .queryParam("q", "Russia")
                                .queryParam("limit", "5")
                                .queryParam("appid", openWeatherAPI.getAppId())
                                .encode()
                                .toUriString()))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(geoResponse));

        mockServer.expect(requestTo(containsString("lat=55.75&lon=37.62")))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(moscowWeatherResponse));

        mockServer.expect(requestTo(containsString("lat=59.93&lon=30.34")))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(spbWeatherResponse));

        List<Weather> weathers = weatherService.searchWeather("Russia");

        assertNotNull(weathers);
        assertEquals(2, weathers.size());
        assertEquals("Moscow", weathers.get(0).getName());
        assertEquals("Saint Petersburg", weathers.get(1).getName());
    }

    @Test
    void getWeather_throwsExceptionWhenAPIUnavailable() {
        mockServer.expect(requestTo(
                        UriComponentsBuilder.fromHttpUrl(openWeatherAPI.getApiCurrentWeatherService())
                                .queryParam("lat", "55.75")
                                .queryParam("lon", "37.62")
                                .queryParam("appid", openWeatherAPI.getAppId())
                                .queryParam("units", "metric")
                                .queryParam("lang", "ru")
                                .encode()
                                .toUriString()))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        Location location = new Location(55.75, 37.62);

        assertThrows(ServiceUnavailableException.class, () -> {
            weatherService.getWeather(location);
        });
    }

}
