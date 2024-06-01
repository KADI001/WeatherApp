package org.kadirov.service;

import lombok.NonNull;
import org.kadirov.dao.location.LocationDAO;
import org.kadirov.entity.location.Location;
import org.kadirov.mapper.WeatherDTOMapper;
import org.kadirov.service.integration.openweatherapi.OpenWeatherMapService;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.util.LocationUtils;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class WeatherServiceImpl implements WeatherService {

    private final OpenWeatherMapService openWeatherMapService;
    private final LocationDAO locationDAO;
    private final WeatherDTOMapper weatherDTOMapper;

    public WeatherServiceImpl(@NonNull OpenWeatherMapService openWeatherMapService,
                              @NonNull LocationDAO locationDAO) {
        this.openWeatherMapService = openWeatherMapService;
        this.locationDAO = locationDAO;
        this.weatherDTOMapper = Mappers.getMapper(WeatherDTOMapper.class);
    }

    @Override
    @NonNull
    public LocationWeather getWeatherByLocation(@NonNull final Location location) {
        return openWeatherMapService.fetchWeather(location.getLatitude(), location.getLongitude());
    }

    @Override
    @NonNull
    public LocationWeather getWeatherByDirectItem(@NonNull final DirectItem directItem) {
        return openWeatherMapService.fetchWeather(directItem.latitude(), directItem.longitude());
    }

    @Override
    @NonNull
    public List<WeatherDTO> getWeatherOfUnfollowedLocations(@NonNull final String locationName, int userId) {
        List<Location> locations = locationDAO.findByUserIdAndOrderById(userId);
        return openWeatherMapService.fetchLocationsByName(locationName).stream()
                .map(directItem -> weatherDTOMapper.weatherAndDirectItemToWeatherDTO(getWeatherByDirectItem(directItem), directItem))
                .filter(weatherDTO -> locations.stream().noneMatch(location -> LocationUtils.equals(location, weatherDTO)))
                .toList();
    }
}
