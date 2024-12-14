package com.slava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
    private String name;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lon")
    private Double lon;
}

