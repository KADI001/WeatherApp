package org.kadirov.service.integration.openweatherapi.exception;

public class OpenWeatherMapApiException extends RuntimeException {
    public OpenWeatherMapApiException() {
    }

    public OpenWeatherMapApiException(String message) {
        super(message);
    }

    public OpenWeatherMapApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenWeatherMapApiException(Throwable cause) {
        super(cause);
    }

    public OpenWeatherMapApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
