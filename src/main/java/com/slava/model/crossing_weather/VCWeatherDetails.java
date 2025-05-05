package com.slava.model.crossing_weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slava.model.imodel.IWeatherDetails;
import lombok.Data;

@Data
public class VCWeatherDetails implements IWeatherDetails {
    @JsonProperty("conditions") private String description;   // "Partially cloudy"
    @JsonProperty("icon")       private String main;          // "partly-cloudy-day"
    private String icon;                                      // полный URL

    @JsonProperty("icon")
    public void buildIcon(String iconName){
        this.main = iconName;
        // готовые SVG/PNG под все 22 иконки есть в Visual Crossing или набор Dr-Lex :contentReference[oaicite:1]{index=1}
        this.icon = "/img/vc/" + iconName + ".svg";
    }
}
