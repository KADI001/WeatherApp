package org.kadirov.service.integration.openweatherapi;

import lombok.NonNull;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.forecast.ForecastResult;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.service.integration.openweatherapi.exception.OpenWeatherMapApiException;

import java.util.List;

public interface OpenWeatherMapService {

    String DIRECT_LOCATIONS_PATH = "/geo/1.0/direct";
    String REVERSE_LOCATIONS_PATH = "/geo/1.0/reverse";
    String WEATHER_PATH = "/data/2.5/weather";
    String FORECAST_PATH = "/data/2.5/forecast";

    List<DirectItem> fetchLocationsByName(@NonNull final String locationName) throws OpenWeatherMapApiException;

    List<DirectItem> fetchLocationsByCoordinates(double latitude, double longitude) throws OpenWeatherMapApiException;

    LocationWeather fetchWeather(double latitude, double longitude) throws OpenWeatherMapApiException;

    ForecastResult fetchForecast(double latitude, double longitude) throws OpenWeatherMapApiException;
}
