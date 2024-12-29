package com.slava.services;

import com.slava.repositories.WeatherDao;
import com.slava.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.junit.jupiter.api.Assertions.*;

@RestClientTest(WeatherService.class)
class OpenWeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void getWeather_returnsParsedResponse() {
        String jsonResponse = "{ \"temp\": 25.5, \"humidity\": 60 }";

        mockServer.expect(requestTo("http://api.openweathermap.org/data/..."))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonResponse));

        WeatherResponse response = openWeatherService.getWeather("location");
        assertNotNull(response);
        assertEquals(25.5, response.getTemp());
        assertEquals(60, response.getHumidity());
    }

    @Test
    void getWeather_throwsExceptionFor4xx() {
        mockServer.expect(requestTo("http://api.openweathermap.org/data/..."))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, () -> openWeatherService.getWeather("location"));
    }
}
