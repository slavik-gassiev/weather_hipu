package com.slava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  Weather {
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
