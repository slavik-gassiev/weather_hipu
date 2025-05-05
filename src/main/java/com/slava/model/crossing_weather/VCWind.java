package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWind;
import lombok.Data;

@Data
public class VCWind implements IWind {
    @JsonProperty("windspeed") private String speed;
}
