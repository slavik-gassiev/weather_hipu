package com.slava.model.crossing_weather;

import com.slava.model.imodel.IWind;
import lombok.Data;

@Data
public class VCWind implements IWind {
    private String speed;
}
