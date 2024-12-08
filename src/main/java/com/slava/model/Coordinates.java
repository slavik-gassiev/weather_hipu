package com.slava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {
    private String name;
    private Double lat;
    private Double lon;
    private String country;
    private String state;
}

