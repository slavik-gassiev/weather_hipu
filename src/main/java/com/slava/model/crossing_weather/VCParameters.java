package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IParameters;
import lombok.Data;

@Data
public class VCParameters implements IParameters {
    @JsonProperty("humidity") private String humidity;
    @JsonProperty("temp")     private String temp;
}
