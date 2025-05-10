package com.slava.model.crossing_weather;

import com.slava.model.imodel.IParameters;
import lombok.Data;

@Data
public class VCParameters implements IParameters {
    private String temp;
    private String humidity;
}
