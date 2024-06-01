package org.kadirov.service.integration.openweatherapi.dto.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastResult (
        @JsonProperty("cod")
        String cod,
        @JsonProperty("message")
        Integer message,
        @JsonProperty("cnt")
        Integer cnt,
        @JsonProperty("list")
        List<ListItem> list,
        @JsonProperty("city")
        City city
) implements Serializable {
}
