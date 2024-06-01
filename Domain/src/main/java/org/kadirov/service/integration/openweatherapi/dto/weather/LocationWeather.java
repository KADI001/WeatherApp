package org.kadirov.service.integration.openweatherapi.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kadirov.service.integration.openweatherapi.dto.common.*;

import java.io.Serializable;
import java.util.List;

public record LocationWeather(
        @JsonProperty("coord")
        Coordinates coordinates,
        @JsonProperty("weather")
        List<Weather> weathers,
        @JsonProperty("base")
        String base,
        @JsonProperty("main")
        Main main,
        @JsonProperty("visibility")
        Integer visibility,
        @JsonProperty("wind")
        Wind wind,
        @JsonProperty("snow")
        Snow snow,
        @JsonProperty("rain")
        Rain rain,
        @JsonProperty("clouds")
        Clouds clouds,
        @JsonProperty("dt")
        Long dateTime,
        @JsonProperty("sys")
        Sys sys,
        @JsonProperty("timezone")
        Integer timezone,
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("cod")
        Integer cod
) implements Serializable {
}
