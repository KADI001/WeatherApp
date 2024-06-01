package org.kadirov.service.integration.openweatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Error(
        @JsonProperty("cod")
        int code,
        @JsonProperty("message")
        String message
) {
        @Override
        public String toString() {
                return "[code=" + code + ", message='" + message + "']";
        }
}
