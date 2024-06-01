package org.kadirov.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.kadirov.config.Environment;

import static java.lang.Integer.parseInt;
import static org.kadirov.util.HttpRequestUtil.deleteCookieByName;
import static org.kadirov.util.TimeUtils.fromMinToSec;

public final class SessionUtils {

    public static final int SESSION_TTL = parseInt(Environment.getProperty("session.ttl.duration"));

    private SessionUtils() {
    }

    public static void refreshSessionCookie(@NonNull final HttpServletRequest req,
                                              @NonNull final HttpServletResponse resp,
                                              @NonNull final String sessionId) {
        deleteCookieByName(req, resp, Cookies.SESSION);
        Cookie newCookie = new Cookie(Cookies.SESSION, sessionId);
        newCookie.setMaxAge(fromMinToSec(SESSION_TTL));
        resp.addCookie(newCookie);
    }
}
