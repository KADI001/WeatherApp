package org.kadirov.util;

import org.kadirov.entity.location.Location;
import org.kadirov.service.WeatherDTO;

public final class LocationUtils {
    private LocationUtils() {
    }

    public static boolean equals(Location location, WeatherDTO weatherDTO) {
        return location.getLatitude().equals(weatherDTO.locationWeather().coordinates().latitude())
                && location.getLongitude().equals(weatherDTO.locationWeather().coordinates().longitude());
    }

}
