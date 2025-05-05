package com.slava.model.open_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWeather;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  Weather implements IWeather{
   @JsonProperty("name")
   private String name;
   @JsonProperty("coord")
   private Coordinates coordinates;
   @JsonProperty("main")
   private Parameters parameters;
   @JsonProperty("weather")
   private List<WeatherDetails> weatherDetails;
   @JsonProperty("wind")
   private Wind wind;
}
