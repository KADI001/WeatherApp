package org.kadirov.service.integration.openweatherapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kadirov.IntegrationTest;
import org.kadirov.OpenWeatherMapResponses;
import org.kadirov.config.Environment;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.service.integration.openweatherapi.dto.forecast.ForecastResult;
import org.kadirov.service.integration.openweatherapi.dto.weather.LocationWeather;
import org.kadirov.service.integration.openweatherapi.exception.OpenWeatherMapApiException;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.matchers.Times;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kadirov.LocationCoordinates.MOSCOW_COORD;
import static org.kadirov.LocationCoordinates.SAINT_PETERSBURG_RU_COORD;
import static org.kadirov.OpenWeatherMapResponses.*;
import static org.kadirov.service.integration.openweatherapi.OpenWeatherMapService.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {80})
class OpenWeatherMapServiceIntegrationTest extends IntegrationTest {

    public static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    public static final String BASE_URL = Environment.getProperty("integration.openweathermap.baseUrl");
    public static final String API_KEY = Environment.getProperty("integration.openweathermap.apiKey");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MockServerClient mockServerClient = new MockServerClient("localhost", 80);

    private static OpenWeatherMapService underTest;

    @BeforeAll
    public static void beforeAll() {
        underTest = new OpenWeatherMapServiceImpl(
                BASE_URL,
                API_KEY
        );
    }

    @AfterAll
    public static void afterAll() {
        mockServerClient.stop();
    }

    @Test
    void GivenRightLocationName_WhenFetchLocationsByName_ThenReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(SAINT_PETERSBURG_LOCATION_ENG_RESP);
        String saintPetersburgLocationNameEng = "Saint Petersburg";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", saintPetersburgLocationNameEng)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(SAINT_PETERSBURG_LOCATION_ENG_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByName(saintPetersburgLocationNameEng);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void GivenNotRightLocationName_WhenFetchLocationsByName_ThenNotReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(SAINT_PETERSBURG_LOCATION_ENG_RESP);
        String moscowLocationNameEng = "Moscow";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", moscowLocationNameEng)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OpenWeatherMapResponses.MOSCOW_LOCATION_ENG_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByName(moscowLocationNameEng);

        //THEN
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void GivenRightRuLocationName_WhenFetchLocationsByName_ThenReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(SAINT_PETERSBURG_LOCATION_RU_RESP);
        String saintPetersburgLocationNameRu = "Санкт Петербург";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", saintPetersburgLocationNameRu)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(SAINT_PETERSBURG_LOCATION_RU_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByName(saintPetersburgLocationNameRu);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void GivenNotRightRuLocationName_WhenFetchLocationsByName_ThenNotReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(SAINT_PETERSBURG_LOCATION_RU_RESP);
        String moscowLocationNameRu = "Москва";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", moscowLocationNameRu)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OpenWeatherMapResponses.MOSCOW_LOCATION_RU_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByName(moscowLocationNameRu);

        //THEN
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void GivenEmptyLocationName_WhenFetchLocationsByName_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        String emptyLocationName = "";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", emptyLocationName)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(EMPTY_LOCATION_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchLocationsByName(emptyLocationName))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='Nothing to geocode']");
    }

    @Test
    void GivenNonExistentLocationName_WhenFetchLocationsByName_ThenReturnNothing() {
        //GIVEN
        String nonExistentLocationName = "NonExistentLocationName";

        mockServerClient
                .when(
                        request()
                                .withPath(DIRECT_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("q", nonExistentLocationName)
                                .withQueryStringParameter("limit")
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(NON_EXISTENT_LOCATION_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByName(nonExistentLocationName);

        //THEN
        assertThat(actual).isEmpty();
    }

    @Test
    void GivenRightCoordinates_WhenFetchLocationsByCoordinates_ThenReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(MOSCOW_LOCATION_ENG_RESP);
        double latitude = MOSCOW_COORD.latitude();
        double longitude = MOSCOW_COORD.longitude();

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(MOSCOW_LOCATION_ENG_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByCoordinates(latitude, longitude);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void GivenNotRightCoordinates_WhenFetchLocationsByCoordinates_ThenNotReturnExpected() throws JsonProcessingException {
        //GIVEN
        List<DirectItem> expected = objectMapper.readerForListOf(DirectItem.class).readValue(MOSCOW_LOCATION_ENG_RESP);
        double latitude = SAINT_PETERSBURG_RU_COORD.latitude();
        double longitude = SAINT_PETERSBURG_RU_COORD.longitude();

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(SAINT_PETERSBURG_LOCATION_ENG_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        List<DirectItem> actual = underTest.fetchLocationsByCoordinates(latitude, longitude);

        //THEN
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void GivenPositiveOutOfRangeLatitude_WhenFetchLocationsByCoordinates_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchLocationsByCoordinates(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLatitude_WhenFetchLocationsByCoordinates_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = -1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchLocationsByCoordinates(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenPositiveOutOfRangeLongitude_WhenFetchLocationsByCoordinates_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = 1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchLocationsByCoordinates(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLongitude_WhenFetchLocationsByCoordinates_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = -1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(REVERSE_LOCATIONS_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchLocationsByCoordinates(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }

    @Test
    void GivenRightCoordinates_WhenFetchWeather_ThenReturnExpected() throws JsonProcessingException {
        //GIVEN
        LocationWeather expected = objectMapper.readValue(MOSCOW_LOCATION_WEATHER_RESP, LocationWeather.class);
        double latitude = MOSCOW_COORD.latitude();
        double longitude = MOSCOW_COORD.longitude();

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(MOSCOW_LOCATION_WEATHER_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        LocationWeather actual = underTest.fetchWeather(latitude, longitude);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void GivenNotRightCoordinates_WhenFetchWeather_ThenNotReturnExpected() throws JsonProcessingException {
        //GIVEN
        LocationWeather expected = objectMapper.readValue(MOSCOW_LOCATION_WEATHER_RESP, LocationWeather.class);
        double latitude = SAINT_PETERSBURG_RU_COORD.latitude();
        double longitude = SAINT_PETERSBURG_RU_COORD.longitude();

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(SAINT_PETERSBURG_RU_LOCATION_WEATHER_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        LocationWeather actual = underTest.fetchWeather(latitude, longitude);

        //THEN
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void GivenPositiveOutOfRangeLatitude_WhenFetchWeather_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchWeather(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLatitude_WhenFetchWeather_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = -1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchWeather(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenPositiveOutOfRangeLongitude_WhenFetchWeather_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = 1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchWeather(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLongitude_WhenFetchWeather_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = -1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(WEATHER_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchWeather(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }

    @Test
    void GivenRightCoordinates_WhenFetchForecast_ThenReturnExpected() throws JsonProcessingException {
        //GIVEN
        ForecastResult expected = objectMapper.readValue(MOSCOW_LOCATION_FORECAST_RESP, ForecastResult.class);
        double latitude = 55.7504461d;
        double longitude = 37.6174943d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(MOSCOW_LOCATION_FORECAST_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        ForecastResult actual = underTest.fetchForecast(latitude, longitude);

        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void GivenNotRightCoordinates_WhenFetchForecast_ThenNotReturnExpected() throws JsonProcessingException {
        //GIVEN
        ForecastResult expected = objectMapper.readValue(MOSCOW_LOCATION_FORECAST_RESP, ForecastResult.class);
        double latitude = 59.938732d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_OK)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(SAINT_PETERSBURG_LOCATION_FORECAST_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        ForecastResult actual = underTest.fetchForecast(latitude, longitude);

        //THEN
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void GivenPositiveOutOfRangeLatitude_WhenFetchForecast_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchForecast(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLatitude_WhenFetchForecast_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = -1155.7504461d;
        double longitude = 30.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LATITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchForecast(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong latitude']");
    }

    @Test
    void GivenPositiveOutOfRangeLongitude_WhenFetchForecast_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = 1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchForecast(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }

    @Test
    void GivenNegativeOutOfRangeLongitude_WhenFetchForecast_ThenThrowOpenWeatherMapApiException() {
        //GIVEN
        double latitude = 59.938732d;
        double longitude = -1230.316229d;

        mockServerClient
                .when(
                        request()
                                .withPath(FORECAST_PATH)
                                .withMethod("GET")
                                .withQueryStringParameter("lat", String.valueOf(latitude))
                                .withQueryStringParameter("lon", String.valueOf(longitude))
                                .withQueryStringParameter("appid", API_KEY),
                        Times.exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(SC_BAD_REQUEST)
                                .withHeader("Content-Type", DEFAULT_CONTENT_TYPE)
                                .withBody(OUT_OF_RANGE_LOCATION_LONGITUDE_RESP)
                                .withDelay(TimeUnit.SECONDS, 1)
                );

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.fetchForecast(latitude, longitude))
                .isInstanceOf(OpenWeatherMapApiException.class)
                .hasMessageContaining("[code=400, message='wrong longitude']");
    }
}