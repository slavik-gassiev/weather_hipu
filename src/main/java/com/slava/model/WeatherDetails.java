package com.slava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWeatherDetails;
import lombok.Data;

@Data
public class WeatherDetails implements IWeatherDetails {
    @JsonProperty("main")
    private String main;
    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;
}
