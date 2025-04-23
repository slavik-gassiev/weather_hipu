package com.slava.model.weatherApi;

import com.slava.model.imodel.IWind;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WAWind implements IWind {
    @JsonProperty("wind_kph")
    private String speed;
}
