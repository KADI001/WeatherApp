package org.kadirov.service.integration.openweatherapi.dto.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Sys(
        @JsonProperty("pod")
        String pod
) implements Serializable {
}
