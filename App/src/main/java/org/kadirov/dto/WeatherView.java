package org.kadirov.dto;

public record WeatherView(
        String locationName,
        String weatherIcon,
        String latitude,
        String longitude,
        String country,
        String measurementDateTime,
        String actualTemp,
        String feelsLikeTemp,
        String cloudiness,
        String humidity,
        String windDegree,
        String windSpeed,
        String tempMin,
        String tempMax,
        String pressure,
        String sunriseDateTime,
        String sunsetDateTime
) {
}
