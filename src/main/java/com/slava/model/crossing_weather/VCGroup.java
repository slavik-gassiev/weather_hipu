package com.slava.model.crossing_weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum VCGroup {
    THUNDER("Гроза", Set.of("thunder-rain", "thunder-showers-day", "thunder-showers-night")),
    RAIN("Дождь", Set.of("rain", "showers-day", "showers-night", "partly-cloudy-day-rain")),
    SNOW("Снег", Set.of("snow", "sleet", "snow-showers-day", "snow-showers-night")),
    FOG("Туман", Set.of("fog", "hail")),
    CLOUDS("Облачно", Set.of("cloudy", "partly-cloudy-day", "partly-cloudy-night")),
    CLEAR("Ясно", Set.of("clear-day", "clear-night"));

    private final String label;
    private final Set<String> codes;

    public static String fromCode(String code) {
        return Arrays.stream(values())
                .filter(g -> g.codes.contains(code))
                .findFirst()
                .map(VCGroup::getLabel)
                .orElse("Другое");
    }
}

