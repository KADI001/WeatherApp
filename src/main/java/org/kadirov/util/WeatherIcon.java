package org.kadirov.util;

import java.util.Arrays;

public enum WeatherIcon {
    CLEAR_SKY_DAY("01d", "clear_sky_d.png"),
    CLEAR_SKY_NIGHT("01n", "clear_sky_n.png"),
    FEW_CLOUDS_DAY("02d", "few_clouds_d.png"),
    FEW_CLOUDS_NIGHT("02n", "few_clouds_n.png"),
    SCATTERED_CLOUDS_DAY("03d", "scattered_clouds_d.png"),
    SCATTERED_CLOUDS_NIGHT("03n", "scattered_clouds_n.png"),
    BROKEN_CLOUDS_DAY("04d", "broken_clouds_d.png"),
    BROKEN_CLOUDS_NIGHT("04n", "broken_clouds_n.png"),
    SHOWER_RAIN_DAY("09d", "shower_rain_d.png"),
    SHOWER_RAIN_NIGHT("09n", "shower_rain_n.png"),
    RAIN_DAY("10d", "rain_d.png"),
    RAIN_NIGHT("10n", "rain_n.png"),
    THUNDERSTORM_DAY("11d", "thunderstorm_d.png"),
    THUNDERSTORM_NIGHT("11n", "thunderstorm_n.png"),
    SNOW_DAY("13d", "snow_d.png"),
    SNOW_NIGHT("13n", "snow_n.png"),
    MIST_DAY("50d", "mist_d.png"),
    MIST_NIGHT("50n", "mist_n.png"),;

    public final String iconName;
    private final String fileName;

    WeatherIcon(String iconName, String fileName) {
        this.iconName = iconName;
        this.fileName = fileName;
    }

    public static String getFileNameByIconName(String iconName) {
        return Arrays.stream(values())
                .filter(weatherIcon -> weatherIcon.iconName.equals(iconName))
                .map(weatherIcon -> weatherIcon.fileName)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No WeatherIcon with icon name " + iconName));
    }
}
