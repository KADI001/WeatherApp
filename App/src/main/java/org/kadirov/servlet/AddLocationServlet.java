package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kadirov.dto.UserLocationDTO;
import org.kadirov.dao.location.LocationDAO;
import org.kadirov.dao.location.LocationDAOImpl;
import org.kadirov.entity.location.Location;
import org.kadirov.service.integration.openweatherapi.OpenWeatherMapService;
import org.kadirov.service.integration.openweatherapi.dto.direct.DirectItem;
import org.kadirov.servlet.exception.BadRequestException;
import org.kadirov.entity.user.User;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.List;

import static org.kadirov.util.Utils.getUserLocationFromRequest;

@Slf4j
@WebServlet(value = "/add-location", name = "AddLocation")
public class AddLocationServlet extends AuthenticatedServlet {

    private LocationDAO locationDAO;
    private OpenWeatherMapService openWeatherMapService;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        locationDAO = (LocationDAOImpl) servletContext.getAttribute("locationDAO");
        openWeatherMapService = (OpenWeatherMapService) servletContext.getAttribute("openWeatherMapService");
    }

    @Override
    protected String processPost(@NonNull final HttpServletRequest req,
                                 @NonNull final HttpServletResponse resp,
                                 @NonNull final WebContext webContext) throws IOException {
        UserLocationDTO userLocationDTO = getUserLocationFromRequest(req);

        List<DirectItem> directItems = openWeatherMapService.fetchLocationsByName(userLocationDTO.paramLocationName());
        boolean exists = directItems.stream()
                .anyMatch(directItem -> directItem.latitude().equals(userLocationDTO.latitude()) && directItem.longitude().equals(userLocationDTO.longitude()));

        if (exists)
            throw new BadRequestException("Request contains bad params");

        User user = new User(userLocationDTO.userId());
        Location newLocation = new Location(userLocationDTO.paramLocationName(), user, userLocationDTO.latitude(), userLocationDTO.longitude());
        locationDAO.save(newLocation);
        resp.sendRedirect("/");
        return null;
    }
}
