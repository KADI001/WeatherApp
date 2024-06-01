package org.kadirov.service.integration.openweatherapi.dto.direct;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.kadirov.deserializer.DoubleDeserializer;

import java.io.Serializable;
import java.util.Map;

public record DirectItem(

        @JsonProperty("name")
        String name,
        @JsonProperty("local_names")
        Map<String, String> localNames,
        @JsonProperty("lat")
        @JsonDeserialize(using = DoubleDeserializer.class)
        Double latitude,
        @JsonProperty("lon")
        @JsonDeserialize(using = DoubleDeserializer.class)
        Double longitude,
        @JsonProperty("country")
        String country,
        @JsonProperty("state")
        String state

) implements Serializable {
}
