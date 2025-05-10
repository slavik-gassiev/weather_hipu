package com.slava.model.crossing_weather;

import com.slava.model.imodel.ICoordinates;
import lombok.Data;

@Data
public class VCCoordinates implements ICoordinates {
    private String name;
    private Double lat;
    private Double lon;
}
