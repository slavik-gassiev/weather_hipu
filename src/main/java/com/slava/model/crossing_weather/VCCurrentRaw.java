package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCCurrentRaw {
    private Double temp;
    private Double humidity;
    private Double windspeed;
    private String icon;
    private String conditions;
}
