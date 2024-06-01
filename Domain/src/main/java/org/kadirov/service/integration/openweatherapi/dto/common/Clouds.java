package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Clouds(
        @JsonProperty("all")
        Integer all
) {
}
