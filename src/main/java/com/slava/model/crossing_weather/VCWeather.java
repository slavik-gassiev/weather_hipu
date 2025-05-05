package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWeather;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCWeather implements IWeather {

    private String name;
    private final VCCoordinates coordinates = new VCCoordinates();
    private final VCParameters  parameters  = new VCParameters();
    private final VCWind        wind        = new VCWind();
    private List<VCWeatherDetails> weatherDetails;

    /* --- маппинг корневых полей --- */
    @JsonProperty("resolvedAddress")
    private void mapName(String n) {
        this.name = n; coordinates.setName(n);
    }

    @JsonProperty("latitude")
    private void mapLat(Double v) {
        coordinates.setLat(v);
    }

    @JsonProperty("longitude")
    private void mapLon(Double v) {
        coordinates.setLon(v);
    }

    /* --- маппинг currentConditions --- */
    @JsonProperty("currentConditions")
    private void mapCurrent(VCCurrentRaw c){
        parameters.setTemp(String.valueOf(c.getTemp()));
        parameters.setHumidity(String.valueOf(c.getHumidity()));
        wind.setSpeed(String.valueOf(c.getWindspeed()));
        VCWeatherDetails det = new VCWeatherDetails();
        det.setDescription(c.getConditions());
        det.buildIcon(c.getIcon());
        this.weatherDetails = List.of(det);
    }
}
