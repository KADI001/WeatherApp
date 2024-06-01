package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(
        @JsonProperty("speed")
        Float speed,
        @JsonProperty("deg")
        Integer deg,
        @JsonProperty("gust")
        Float gust
) {
}
