package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.kadirov.dto.WeatherView;
import org.kadirov.dao.location.LocationDAO;
import org.kadirov.dao.location.LocationDAOImpl;
import org.kadirov.entity.location.Location;
import org.kadirov.mapper.WeatherViewMapper;
import org.kadirov.service.WeatherService;
import org.kadirov.entity.session.Session;
import org.mapstruct.factory.Mappers;
import org.thymeleaf.context.WebContext;

import java.util.List;

@WebServlet(value = "", name = "Home")
public class HomeServlet extends BaseServlet {

    private WeatherService weatherService;
    private LocationDAO locationDAO;
    private WeatherViewMapper weatherViewMapper;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        weatherService = (WeatherService) servletContext.getAttribute("weatherService");
        locationDAO = (LocationDAOImpl) servletContext.getAttribute("locationDAO");
        weatherViewMapper = Mappers.getMapper(WeatherViewMapper.class);
    }

    @Override
    protected String processGet(@NonNull final HttpServletRequest req,
                                @NonNull final HttpServletResponse resp,
                                @NonNull final WebContext webContext) {
        HttpSession httpSession = req.getSession();
        Session session = (Session) httpSession.getAttribute("session");

        if (session != null) {
            List<Location> locations = locationDAO.findByUserIdAndOrderById(session.getUser().getId());
            List<WeatherView> cards = locations.stream()
                    .map(location -> weatherViewMapper.locationWeatherAndLocationToWeatherView(
                            weatherService.getWeatherByLocation(location),
                            location
                    ))
                    .toList();

            webContext.setVariable("cards", cards);
            webContext.setVariable("user", session.getUser());
            webContext.setVariable("isSearching", false);
            webContext.setVariable("isAuthorized", true);
        }

        return "home";
    }
}
