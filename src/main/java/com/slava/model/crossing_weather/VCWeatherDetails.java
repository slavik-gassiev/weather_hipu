package com.slava.model.crossing_weather;

import com.slava.model.imodel.IWeatherDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCWeatherDetails implements IWeatherDetails {

    private String main;
    private String description;
    private String icon;   // окончательный URL

    private static final String CDN_PREFIX =
            "https://cdn.jsdelivr.net/gh/visualcrossing/WeatherIcons@main/PNG/2nd%20Set%20-%20Color/";

    @Override
    public void setIcon(String value) {
        value = URLDecoder.decode(value, StandardCharsets.UTF_8);
        this.icon = value.startsWith("http")
                ? value
                : CDN_PREFIX + value + ".png";
    }
}
