package com.slava.model.weatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.*;
import com.slava.model.weatherApi.WACoordinates;
import com.slava.model.weatherApi.WAParameters;
import com.slava.model.weatherApi.WAWeatherDetails;
import com.slava.model.weatherApi.WAWind;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)      // игнорируем поля, которых нет в интерфейсе
public class WAWeather implements IWeather {

    /* -------- поля, идентичные классу Weather -------- */
    private String name;
    private WACoordinates coordinates;
    private WAParameters parameters;
    private List<WAWeatherDetails> weatherDetails;
    private WAWind wind;

    /** 1) блок location → name + coordinates */
    @JsonProperty("location")
    private void unpackLocation(Map<String, Object> loc) {
        this.name = (String) loc.get("name");

        this.coordinates = new WACoordinates();
        this.coordinates.setName(this.name);
        this.coordinates.setLat( toDouble(loc.get("lat")) );
        this.coordinates.setLon( toDouble(loc.get("lon")) );
    }

    /** 2) блок current → parameters + wind + weatherDetails */
    @JsonProperty("current")
    @SuppressWarnings("unchecked")
    private void unpackCurrent(Map<String, Object> cur) {

        this.parameters = new WAParameters();
        this.parameters.setTemp( cur.get("temp_c").toString() );
        this.parameters.setHumidity( cur.get("humidity").toString() );

        this.wind = new WAWind();
        this.wind.setSpeed( cur.get("wind_kph").toString() );

        /* condition → WeatherDetails (список из одного элемента) */
        Map<String, Object> cond = (Map<String, Object>) cur.get("condition");
        WAWeatherDetails details = new WAWeatherDetails();
        details.setMain(WAGroup.fromCode((Integer) cond.get("code")));          // «Patchy rain nearby»
        details.setDescription( (String) cond.get("text") );
        details.setIcon( (String) cond.get("icon") );

        this.weatherDetails = List.of(details);
    }

    /* helper */
    private static Double toDouble(Object o) {
        return o == null ? null : Double.valueOf(o.toString());
    }
}
