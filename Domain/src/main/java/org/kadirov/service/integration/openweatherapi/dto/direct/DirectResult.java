package org.kadirov.service.integration.openweatherapi.dto.direct;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record DirectResult(
        @JsonProperty
        List<DirectItem> directItems
) implements Serializable {
}
