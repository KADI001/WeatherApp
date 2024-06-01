package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(
        @JsonProperty("lat")
        Double latitude,
        @JsonProperty("lon")
        Double longitude
) {
}
