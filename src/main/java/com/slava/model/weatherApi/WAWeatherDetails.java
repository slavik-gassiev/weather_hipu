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
    private String main;

    @JsonProperty("text")
    private String description;

    private String icon;

    @JsonProperty("code")
    private void unpackCode(int code) {
        this.main = WAGroup.fromCode(code);   // «Clouds»
    }

    @JsonSetter("icon")
    private void setRawIcon(String rawUrl) {
        this.icon = rawUrl.startsWith("http") ? rawUrl : "https:" + rawUrl;
    }
}
