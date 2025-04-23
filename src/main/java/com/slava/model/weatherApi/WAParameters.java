package com.slava.model.weatherApi;

import com.slava.model.imodel.IParameters;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WAParameters implements IParameters {
    @JsonProperty("humidity")
    private String humidity;

    @JsonProperty("temp_c")
    private String temp;
}
