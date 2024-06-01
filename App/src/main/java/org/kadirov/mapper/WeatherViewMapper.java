package org.kadirov.mapper;

import org.kadirov.dto.WeatherView;
import org.kadirov.entity.location.Location;
import org.kadirov.service.WeatherDTO;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.util.WeatherUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {WeatherUtils.class, String.class})
public interface WeatherViewMapper {

    @Mapping(target = "locationName", source = "directItem.name")
    @Mapping(target = "weatherIcon", expression = "java(locationWeather.weathers().get(0).icon())")
    @Mapping(target = "latitude", source = "locationWeather.coordinates.latitude")
    @Mapping(target = "longitude", source = "locationWeather.coordinates.longitude")
    @Mapping(target = "country", source = "locationWeather.sys.country")
    @Mapping(target = "measurementDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.dateTime(), locationWeather.timezone()))")
    @Mapping(target = "actualTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().temp())))")
    @Mapping(target = "feelsLikeTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().feelsLike())))")
    @Mapping(target = "cloudiness", source = "locationWeather.clouds.all")
    @Mapping(target = "humidity", source = "locationWeather.main.humidity")
    @Mapping(target = "windDegree", source = "locationWeather.wind.deg")
    @Mapping(target = "windSpeed", expression = "java(String.valueOf(WeatherUtils.roundToOne(locationWeather.wind().speed())))")
    @Mapping(target = "tempMin", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().tempMin())))")
    @Mapping(target = "tempMax", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().tempMax())))")
    @Mapping(target = "pressure", source = "locationWeather.main.pressure")
    @Mapping(target = "sunriseDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.sys().sunrise(), locationWeather.timezone()))")
    @Mapping(target = "sunsetDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.sys().sunset(), locationWeather.timezone()))")
    WeatherView locationWeatherAndDirectItemToWeatherView(LocationWeather locationWeather, DirectItem directItem);

    @Mapping(target = "locationName", source = "location.name")
    @Mapping(target = "weatherIcon", expression = "java(locationWeather.weathers().get(0).icon())")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "country", source = "locationWeather.sys.country")
    @Mapping(target = "measurementDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.dateTime(), locationWeather.timezone()))")
    @Mapping(target = "actualTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().temp())))")
    @Mapping(target = "feelsLikeTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().feelsLike())))")
    @Mapping(target = "cloudiness", source = "locationWeather.clouds.all")
    @Mapping(target = "humidity", source = "locationWeather.main.humidity")
    @Mapping(target = "windDegree", source = "locationWeather.wind.deg")
    @Mapping(target = "windSpeed", expression = "java(String.valueOf(WeatherUtils.roundToOne(locationWeather.wind().speed())))")
    @Mapping(target = "tempMin", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().tempMin())))")
    @Mapping(target = "tempMax", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(locationWeather.main().tempMax())))")
    @Mapping(target = "pressure", source = "locationWeather.main.pressure")
    @Mapping(target = "sunriseDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.sys().sunrise(), locationWeather.timezone()))")
    @Mapping(target = "sunsetDateTime", expression = "java(WeatherUtils.formatDate(locationWeather.sys().sunset(), locationWeather.timezone()))")
    WeatherView locationWeatherAndLocationToWeatherView(LocationWeather locationWeather, Location location);

    @Mapping(target = "locationName", source = "weatherDTO.locationName")
    @Mapping(target = "weatherIcon", expression = "java(weatherDTO.locationWeather().weathers().get(0).icon())")
    @Mapping(target = "latitude", source = "weatherDTO.locationWeather.coordinates.latitude")
    @Mapping(target = "longitude", source = "weatherDTO.locationWeather.coordinates.longitude")
    @Mapping(target = "country", source = "weatherDTO.locationWeather.sys.country")
    @Mapping(target = "measurementDateTime", expression = "java(WeatherUtils.formatDate(weatherDTO.locationWeather().dateTime(), weatherDTO.locationWeather().timezone()))")
    @Mapping(target = "actualTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(weatherDTO.locationWeather().main().temp())))")
    @Mapping(target = "feelsLikeTemp", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(weatherDTO.locationWeather().main().feelsLike())))")
    @Mapping(target = "cloudiness", source = "weatherDTO.locationWeather.clouds.all")
    @Mapping(target = "humidity", source = "weatherDTO.locationWeather.main.humidity")
    @Mapping(target = "windDegree", source = "weatherDTO.locationWeather.wind.deg")
    @Mapping(target = "windSpeed", expression = "java(String.valueOf(WeatherUtils.roundToOne(weatherDTO.locationWeather().wind().speed())))")
    @Mapping(target = "tempMin", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(weatherDTO.locationWeather().main().tempMin())))")
    @Mapping(target = "tempMax", expression = "java(String.valueOf(WeatherUtils.convertFromKelvinToCelsius(weatherDTO.locationWeather().main().tempMax())))")
    @Mapping(target = "pressure", source = "weatherDTO.locationWeather.main.pressure")
    @Mapping(target = "sunriseDateTime", expression = "java(WeatherUtils.formatDate(weatherDTO.locationWeather().sys().sunrise(), weatherDTO.locationWeather().timezone()))")
    @Mapping(target = "sunsetDateTime", expression = "java(WeatherUtils.formatDate(weatherDTO.locationWeather().sys().sunset(), weatherDTO.locationWeather().timezone()))")
    WeatherView weatherDtoToWeatherView(WeatherDTO weatherDTO);

}
