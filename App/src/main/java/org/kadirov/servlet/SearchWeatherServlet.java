package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.kadirov.dto.WeatherView;
import org.kadirov.mapper.WeatherViewMapper;
import org.kadirov.service.WeatherService;
import org.kadirov.servlet.exception.BadRequestException;
import org.kadirov.entity.session.Session;
import org.mapstruct.factory.Mappers;
import org.thymeleaf.context.WebContext;

import java.util.List;

import static java.lang.String.format;
import static org.kadirov.util.ExceptionMessages.VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN;
import static org.kadirov.util.ValidationUtil.validateStringParameter;


@WebServlet(value = "/search", name = "SearchWeather")
public class SearchWeatherServlet extends AuthenticatedServlet {

    public static final String LOCATION_NAME_PARAM = "locationName";

    private WeatherService weatherService;
    private WeatherViewMapper weatherViewMapper;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        weatherService = (WeatherService) servletContext.getAttribute("weatherService");
        weatherViewMapper = Mappers.getMapper(WeatherViewMapper.class);
    }

    @Override
    protected String processGet(final @NonNull HttpServletRequest req,
                                final @NonNull HttpServletResponse resp,
                                final @NonNull WebContext webContext) {
        String locationName = req.getParameter(LOCATION_NAME_PARAM);

        if (!validateStringParameter(locationName))
            throw new BadRequestException(format(VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN, LOCATION_NAME_PARAM));

        HttpSession httpSession = req.getSession();
        Session session = (Session) httpSession.getAttribute("session");

        int userId = session.getUser().getId();
        List<WeatherView> cards = weatherService.getWeatherOfUnfollowedLocations(locationName, userId).stream()
                .map(weatherDTO -> weatherViewMapper.weatherDtoToWeatherView(weatherDTO))
                .toList();

        webContext.setVariable("isSearching", true);
        webContext.setVariable("isAuthorized", true);
        webContext.setVariable("user", session.getUser());
        webContext.setVariable("cards", cards);

        return "home";
    }
}
