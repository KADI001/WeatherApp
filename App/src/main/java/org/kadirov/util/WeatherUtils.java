package org.kadirov.util;

import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.*;

public final class WeatherUtils {

    public static final DateTimeFormatter WEATHER_LOCAL_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .toFormatter();

    public static final int ROUNDED_TO = 1;

    private WeatherUtils(){}

    public static @NonNull String formatDate(Long dateTime, int timezoneOffset){
        OffsetDateTime offsetDateTime = Instant
                .ofEpochMilli(dateTime * 1000L)
                .atOffset(ZoneOffset.ofTotalSeconds(timezoneOffset));

        LocalTime localTime = offsetDateTime.toLocalTime();

        return localTime.format(WEATHER_LOCAL_TIME_FORMATTER);
    }

    public static Double convertFromKelvinToCelsius(float tempKelvin){
        double scale = Math.pow(10, ROUNDED_TO);
        return Math.round((tempKelvin - 273.15f) * scale) / scale;
    }

    public static Double roundToOne(double number){
        double scale = Math.pow(10, 1);
        return Math.round(number * scale) / scale;
    }
}
