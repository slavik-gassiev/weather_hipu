package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.slava.model.imodel.ICoordinates;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCCoordinates implements ICoordinates {
    private String name;
    private Double lat;
    private Double lon;
}
