package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VCWeather implements IWeather {

    private String name;
    private VCCoordinates coordinates;
    private VCParameters parameters;
    private List<VCWeatherDetails> weatherDetails;
    private VCWind wind;

    @JsonProperty("resolvedAddress")
    public void setResolvedAddress(String resolvedAddress) {
        if (this.coordinates == null) {
            this.coordinates = new VCCoordinates();
        }
        this.coordinates.setName(resolvedAddress);

        // если это координаты — всё равно ставим name, как fallback
        this.name = isCoordinateFormat(resolvedAddress) ? "Без названия" : resolvedAddress;
    }

    // Хелпер для проверки формата "число,число"
    private boolean isCoordinateFormat(String input) {
        return input.matches("^-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?$");
    }


    @JsonProperty("latitude")
    public void setLat(double lat) {
        if (this.coordinates == null) this.coordinates = new VCCoordinates();
        this.coordinates.setLat(lat);
    }

    @JsonProperty("longitude")
    public void setLon(double lon) {
        if (this.coordinates == null) this.coordinates = new VCCoordinates();
        this.coordinates.setLon(lon);
    }

    @JsonProperty("currentConditions")
    @SuppressWarnings("unchecked")
    public void unpackCurrentConditions(Map<String, Object> current) {
        this.parameters = new VCParameters();
        this.parameters.setTemp(current.get("temp").toString());
        this.parameters.setHumidity(current.get("humidity").toString());

        this.wind = new VCWind();
        this.wind.setSpeed(current.get("windspeed").toString());

        VCWeatherDetails details = new VCWeatherDetails();

        String iconCode = (String) current.get("icon");          // cloudy / clear-day …
        if (iconCode != null) {
            details.setMain(VCGroup.fromCode(iconCode));

            details.setIcon(iconCode);
        }

        details.setDescription((String) current.get("conditions"));

        this.weatherDetails = List.of(details);
    }
}

