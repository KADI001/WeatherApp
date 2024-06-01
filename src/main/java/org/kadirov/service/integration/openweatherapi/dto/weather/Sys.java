package org.kadirov.service.integration.openweatherapi.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Sys(
        @JsonProperty("type")
        Integer type,
        @JsonProperty("id")
        Long id,
        @JsonProperty("country")
        String country,
        @JsonProperty("sunrise")
        Long sunrise,
        @JsonProperty("sunset")
        Long sunset
) {
}
