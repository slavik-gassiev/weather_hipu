package com.slava.model.open_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.ICoordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates implements ICoordinates {
    private String name;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lon")
    private Double lon;
}

