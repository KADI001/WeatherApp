package org.kadirov.util;

import jakarta.servlet.http.HttpServletRequest;
import org.kadirov.dto.UserLocationDTO;
import org.kadirov.servlet.exception.BadRequestException;
import org.kadirov.entity.session.Session;

import static java.lang.String.format;
import static org.kadirov.util.ExceptionMessages.VALIDATION_PARAMETER_NOT_VALID_FORMAT_PATTERN;
import static org.kadirov.util.ExceptionMessages.VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN;
import static org.kadirov.util.ValidationUtil.validateStringParameter;

public final class Utils {

    public static UserLocationDTO getUserLocationFromRequest(HttpServletRequest req) {
        String paramLocationName = req.getParameter("locationName");
        String paramLatitude = req.getParameter("lat");
        String paramLongitude = req.getParameter("lon");

        if (!validateStringParameter(paramLocationName)) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN, "name"));
        }

        if (!validateStringParameter(paramLatitude)) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN, "lat"));
        }

        if (!validateStringParameter(paramLongitude)) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_OMITTED_OR_NOT_VALID_PATTERN, "lon"));
        }

        double latitude;
        double longitude;

        try {
            latitude = Double.parseDouble(paramLatitude);
        } catch (Exception e) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_NOT_VALID_FORMAT_PATTERN, "lat"), e);
        }

        try {
            longitude = Double.parseDouble(paramLongitude);
        } catch (Exception e) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_NOT_VALID_FORMAT_PATTERN, "lon"), e);
        }

        if (latitude < -180 || latitude > 180) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_NOT_VALID_FORMAT_PATTERN, "lat"));
        }

        if (longitude < -180 || longitude > 180) {
            throw new BadRequestException(format(VALIDATION_PARAMETER_NOT_VALID_FORMAT_PATTERN, "lon"));
        }

        Session session = (Session) req.getSession().getAttribute("session");

        return new UserLocationDTO(paramLocationName, session.getUser().getId(), latitude, longitude);
    }

}
