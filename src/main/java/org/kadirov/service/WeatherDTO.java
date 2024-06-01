package org.kadirov.service;

import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;

public record WeatherDTO(
        String locationName,
        LocationWeather locationWeather
) {
}
