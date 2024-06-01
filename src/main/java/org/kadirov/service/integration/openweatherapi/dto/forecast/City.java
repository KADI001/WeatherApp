package org.kadirov.service.integration.openweatherapi.dto.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kadirov.service.integration.openweatherapi.dto.common.Coordinates;

import java.io.Serializable;

public record City(
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("coord")
        Coordinates coordinates,
        @JsonProperty("country")
        String country,
        @JsonProperty("population")
        Integer population,
        @JsonProperty("timezone")
        Integer timezone,
        @JsonProperty("sunrise")
        Long sunrise,
        @JsonProperty("sunset")
        Long sunset
) implements Serializable {
}
