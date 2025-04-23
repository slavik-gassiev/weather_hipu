package com.slava.model.weatherApi;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.slava.model.imodel.IWeatherDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WAWeatherDetails implements IWeatherDetails {
    @JsonIgnore
    @JsonProperty("code")
    private String main;

    @JsonProperty("text")
    private String description;

    private String icon;

    private void unpackIcon(String rawUrl) {
        this.icon = "https:" + rawUrl;
    }
}
