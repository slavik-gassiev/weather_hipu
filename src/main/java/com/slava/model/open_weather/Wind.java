package com.slava.model.open_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWind;
import lombok.Data;

@Data
public class Wind implements IWind {
    @JsonProperty("speed")
    private String speed;
}
