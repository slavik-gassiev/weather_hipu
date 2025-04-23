package com.slava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IParameters;
import lombok.Data;

import java.util.List;

@Data
public class Parameters implements IParameters {
    @JsonProperty("humidity")
    private String humidity;
    @JsonProperty("temp")
    private String temp;
}
