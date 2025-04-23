package com.slava.model.weatherApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.ICoordinates;
import lombok.Data;

@Data
public class WACoordinates implements ICoordinates {
    @JsonProperty("name")
    private String name;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lon")
    private Double lon;
}
