package com.slava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Parameters {
    private String main;
    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("humidity")
    private String humidity;
    @JsonProperty("temp")
    private String temp;
}
