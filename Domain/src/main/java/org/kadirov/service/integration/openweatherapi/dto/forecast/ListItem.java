package org.kadirov.service.integration.openweatherapi.dto.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kadirov.service.integration.openweatherapi.dto.common.*;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListItem(
        @JsonProperty("dt")
        Long dt,
        @JsonProperty("main")
        Main main,
        @JsonProperty("weather")
        List<Weather> weather,
        @JsonProperty("clouds")
        Clouds clouds,
        @JsonProperty("wind")
        Wind wind,
        @JsonProperty("visibility")
        Integer visibility,
        @JsonProperty("pop")
        Float pop,
        @JsonProperty("rain")
        Rain rain,
        @JsonProperty("snow")
        Snow snow,
        @JsonProperty("sys")
        Sys sys,
        @JsonProperty("dt_txt")
        String dtTxt
) implements Serializable {
}
