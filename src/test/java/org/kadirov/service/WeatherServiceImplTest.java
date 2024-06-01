package org.kadirov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kadirov.dao.location.LocationDAO;
import org.kadirov.entity.location.Location;
import org.kadirov.mapper.WeatherDTOMapper;
import org.kadirov.service.integration.openweatherapi.OpenWeatherMapService;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.entity.user.User;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kadirov.LocationCoordinates.*;
import static org.kadirov.OpenWeatherMapResponses.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class WeatherServiceImplTest {

    private static final WeatherDTOMapper weatherDTOMapper = Mappers.getMapper(WeatherDTOMapper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OpenWeatherMapService openWeatherMapService;
    @Mock
    private LocationDAO locationDAO;

    @InjectMocks
    private WeatherServiceImpl underTest;

    @Test
    void getWeatherOfUnfollowedLocations() throws JsonProcessingException {
        //GIVENc
        String locationName = "Saint Petersburg";
        int userId = 1;
        List<DirectItem> directItems = objectMapper.readerForListOf(DirectItem.class).readValue(SAINT_PETERSBURG_LOCATION_ENG_RESP);
        LocationWeather saintPetersburgRuWeather = objectMapper.readValue(SAINT_PETERSBURG_RU_LOCATION_WEATHER_RESP, LocationWeather.class);
        LocationWeather saintPetersburgFUWeather = objectMapper.readValue(SAINT_PETERSBURG_FLORIDA_US_LOCATION_WEATHER_RESP, LocationWeather.class);
        LocationWeather saintPetersburgPUWeather = objectMapper.readValue(SAINT_PETERSBURG_PENNSYLVANIA_US_LOCATION_WEATHER_RESP, LocationWeather.class);
        LocationWeather saintPetersburgCUWeather = objectMapper.readValue(SAINT_PETERSBURG_COLORADO_US_LOCATION_WEATHER_RESP, LocationWeather.class);

        List<WeatherDTO> expected = new ArrayList<>();
        expected.add(weatherDTOMapper.weatherAndDirectItemToWeatherDTO(saintPetersburgFUWeather, directItems.get(1)));
        expected.add(weatherDTOMapper.weatherAndDirectItemToWeatherDTO(saintPetersburgPUWeather, directItems.get(2)));
        expected.add(weatherDTOMapper.weatherAndDirectItemToWeatherDTO(saintPetersburgCUWeather, directItems.get(3)));

        when(locationDAO.findByUserIdAndOrderById(userId))
                .thenReturn(List.of(
                        new Location(locationName, new User(userId), SAINT_PETERSBURG_RU_COORD.latitude(), SAINT_PETERSBURG_RU_COORD.longitude())
                ));

        when(openWeatherMapService.fetchLocationsByName(locationName))
                .thenReturn(directItems);

        when(openWeatherMapService.fetchWeather(SAINT_PETERSBURG_RU_COORD.latitude(), SAINT_PETERSBURG_RU_COORD.longitude()))
                .thenReturn(saintPetersburgRuWeather);
        when(openWeatherMapService.fetchWeather(SAINT_PETERSBURG_FLORIDA_US_COORD.latitude(), SAINT_PETERSBURG_FLORIDA_US_COORD.longitude()))
                .thenReturn(saintPetersburgFUWeather);
        when(openWeatherMapService.fetchWeather(SAINT_PETERSBURG_PENNSYLVANIA_US_COORD.latitude(), SAINT_PETERSBURG_PENNSYLVANIA_US_COORD.longitude()))
                .thenReturn(saintPetersburgPUWeather);
        when(openWeatherMapService.fetchWeather(SAINT_PETERSBURG_COLORADO_US_COORD.latitude(), SAINT_PETERSBURG_COLORADO_US_COORD.longitude()))
                .thenReturn(saintPetersburgCUWeather);

        //WHEN
        List<WeatherDTO> actual = underTest.getWeatherOfUnfollowedLocations(locationName, userId);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }
}