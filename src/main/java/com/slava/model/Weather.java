package com.slava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
   private String name;
   private Double lat;
   private Double lon;
   private int temperature;
   private int humidity;
   private int windSpeed;
   
}
