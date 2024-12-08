package com.slava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
   private Coordinates coordinates;
   private String main;
   private String icon;
   private int temp;
   private int humidity;
   private int windSpeed;
   
}
