package org.kadirov.service.integration.openweatherapi.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Main(
        @JsonProperty("temp")
        Float temp,
        @JsonProperty("feels_like")
        Float feelsLike,
        @JsonProperty("temp_min")
        Float tempMin,
        @JsonProperty("temp_max")
        Float tempMax,
        @JsonProperty("pressure")
        Integer pressure,
        @JsonProperty("humidity")
        Integer humidity,
        @JsonProperty("sea_level")
        Integer seaLevel,
        @JsonProperty("grnd_level")
        Integer groundLevel
) {
}
