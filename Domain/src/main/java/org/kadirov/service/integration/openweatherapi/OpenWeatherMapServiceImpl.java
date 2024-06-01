package org.kadirov.service.integration.openweatherapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.kadirov.service.integration.openweatherapi.dto.Error;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.forecast.ForecastResult;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.service.integration.openweatherapi.exception.OpenWeatherMapApiException;
import org.kadirov.util.ThrowableFunction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

public class OpenWeatherMapServiceImpl implements OpenWeatherMapService {

    private final String baseURL;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenWeatherMapServiceImpl(@NonNull String baseURL, @NonNull String apiKey) {
        this.baseURL = baseURL;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<DirectItem> fetchLocationsByName(@NonNull final String locationName) throws OpenWeatherMapApiException {
        Map<String, Object> params = new HashMap<>();
        params.put("q", locationName);
        params.put("limit", 5);
        params.put("appid", apiKey);

        return doRequest(DIRECT_LOCATIONS_PATH, params, (response) -> objectMapper.readerForListOf(DirectItem.class).readValue(response));
    }

    @Override
    public List<DirectItem> fetchLocationsByCoordinates(double latitude, double longitude) throws OpenWeatherMapApiException {
        Map<String, Object> params = new HashMap<>();
        params.put("lat", latitude);
        params.put("lon", longitude);
        params.put("limit", 5);
        params.put("appid", apiKey);

        return doRequest(REVERSE_LOCATIONS_PATH, params, (response) -> objectMapper.readerForListOf(DirectItem.class).readValue(response));
    }

    @Override
    public LocationWeather fetchWeather(double latitude, double longitude) throws OpenWeatherMapApiException {
        Map<String, Object> params = new HashMap<>();
        params.put("lat", latitude);
        params.put("lon", longitude);
        params.put("appid", apiKey);

        return doRequest(WEATHER_PATH, params, (response) -> objectMapper.readValue(response, LocationWeather.class));
    }

    @Override
    public ForecastResult fetchForecast(double latitude, double longitude) throws OpenWeatherMapApiException {
        Map<String, Object> params = new HashMap<>();
        params.put("lat", latitude);
        params.put("lon", longitude);
        params.put("appid", apiKey);

        return doRequest(FORECAST_PATH, params, (response) -> objectMapper.readValue(response, ForecastResult.class));
    }

    private <T> T doRequest(@NonNull String path, @NonNull Map<String, Object> params, @NonNull ThrowableFunction<String, T> responseMapper) {
        T mapped;
        Error error;

        try {
            String queryParams = params.entrySet().stream()
                    .map(entry -> format("%s=%s", entry.getKey(), encode(String.valueOf(entry.getValue()), UTF_8)))
                    .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest
                    .newBuilder(URI.create(baseURL + path + "?" + queryParams))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            String responseBody = response.body();
            int statusCode = response.statusCode();

            mapped = statusCode == SC_OK ? responseMapper.apply(responseBody) : null;
            error = statusCode != SC_OK ? objectMapper.readValue(responseBody, Error.class) : null;
        } catch (Exception e) {
            throw new OpenWeatherMapApiException(e.getMessage(), e);
        }

        if (error != null) {
            throw new OpenWeatherMapApiException(format("OpenWeatherMapApi error %s", error));
        }

        return mapped;
    }
}
