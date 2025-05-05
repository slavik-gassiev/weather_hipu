package com.slava.model.crossing_weather;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class VCWeatherAPI {
    @Value("${vc.api.base-url}")
    private String baseUrl;
    @Value("${vc.api.key}")
    private String key;
    @Value("${vc.api.unit-group}")
    private String unitGroup;
    @Value("${vc.api.include}")
    private String include;
    @Value("${vc.api.lang}")
    private String lang;
}
