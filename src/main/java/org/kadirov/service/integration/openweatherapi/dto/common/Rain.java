package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rain(
        @JsonProperty("1h")
        Float oneHour,
        @JsonProperty("3h")
        Float threeHour
) {
}
