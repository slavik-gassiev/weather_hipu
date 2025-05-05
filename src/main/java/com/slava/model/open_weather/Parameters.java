package com.slava.model.open_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IParameters;
import lombok.Data;

@Data
public class Parameters implements IParameters {
    @JsonProperty("humidity")
    private String humidity;
    @JsonProperty("temp")
    private String temp;
}
