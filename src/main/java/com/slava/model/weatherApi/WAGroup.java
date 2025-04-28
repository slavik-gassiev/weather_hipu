package com.slava.model.weatherApi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum WAGroup {
    THUNDER("Гроза", Set.of(1087, 1273, 1276)),
    RAIN("Дождь",         Set.of(1063, 1150, 1180, 1183, 1195)),
    SNOW("Снег",         Set.of(1114, 1210, 1213, 1222)),
    CLOUDS("Облачно",     Set.of(1003, 1006, 1009)),
    CLEAR("Ясно",       Set.of(1000));

    private final String label;
    private final Set<Integer> codes;

    public static String fromCode(int code) {
        return Arrays.stream(values())
                .filter(g -> g.codes.contains(code))
                .findFirst()
                .map(WAGroup::getLabel)
                .orElse("Другое");
    }
}

