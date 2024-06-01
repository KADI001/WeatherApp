package org.kadirov.mapper;

import org.kadirov.service.WeatherDTO;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WeatherDTOMapper {

    @Mapping(target = "locationName", source = "directItem.name")
    @Mapping(target = "locationWeather", source = "locationWeather")
    WeatherDTO weatherAndDirectItemToWeatherDTO(LocationWeather locationWeather, DirectItem directItem);
}
