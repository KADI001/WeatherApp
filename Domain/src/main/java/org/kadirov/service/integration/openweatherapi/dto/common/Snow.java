package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Snow(
        @JsonProperty("1h")
        Float oneHour,
        @JsonProperty("3h")
        Float threeHour
) {
}
