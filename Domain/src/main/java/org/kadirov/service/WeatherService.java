package org.kadirov.service;

import lombok.NonNull;
import org.kadirov.entity.location.Location;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;

import java.util.List;

public interface WeatherService {
    @NonNull
    LocationWeather getWeatherByLocation(@NonNull final Location location);
    @NonNull
    LocationWeather getWeatherByDirectItem(@NonNull final DirectItem directItem);
    @NonNull
    List<WeatherDTO> getWeatherOfUnfollowedLocations(@NonNull final String locationName, int userId);
}
