package org.kadirov.service.integration.openweatherapi.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Temp(
        @JsonProperty("day")
        Float day,
        @JsonProperty("min")
        Float min,
        @JsonProperty("max")
        Float max,
        @JsonProperty("night")
        Float night,
        @JsonProperty("eve")
        Float eve,
        @JsonProperty("morn")
        Float morn
) implements Serializable {
}
