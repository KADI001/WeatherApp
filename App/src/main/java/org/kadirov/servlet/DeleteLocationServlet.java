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
import org.thymeleaf.context.WebContext;

import java.io.IOException;

import static org.kadirov.util.Utils.getUserLocationFromRequest;

@Slf4j
@WebServlet(value = "/delete-location", name = "DeleteLocation")
public class DeleteLocationServlet extends AuthenticatedServlet {

    private LocationDAO locationDAO;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        locationDAO = (LocationDAOImpl) servletContext.getAttribute("locationDAO");
    }

    @Override
    protected String processPost(@NonNull final HttpServletRequest req,
                                 @NonNull final HttpServletResponse resp,
                                 @NonNull final WebContext webContext) throws RuntimeException, IOException {
        UserLocationDTO userLocation = getUserLocationFromRequest(req);

        if (!locationDAO.deleteByLongAndLatAndUserId(userLocation.longitude(), userLocation.latitude(), userLocation.userId()))
            log.debug("Couldn't delete location [lat: {} | lon: {} | userId: {}]", userLocation.latitude(), userLocation.longitude(), userLocation.userId());

        resp.sendRedirect("/");
        return null;
    }
}
