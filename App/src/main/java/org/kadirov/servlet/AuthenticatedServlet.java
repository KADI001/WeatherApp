package org.kadirov.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kadirov.servlet.exception.ForbiddenException;
import org.kadirov.entity.session.Session;

import static org.kadirov.servlet.exception.ExceptionNav.redirectTo;
import static org.kadirov.util.HttpRequestUtil.getEncodedFullURL;
import static org.kadirov.util.ValidationUtil.isSessionExpired;

public abstract class AuthenticatedServlet extends BaseServlet {

    public static final String SESSION_ATTRIBUTE_NAME = "session";

    @Override
    public void onPreService(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();
        Session session = (Session) httpSession.getAttribute(SESSION_ATTRIBUTE_NAME);
        String redirectUrl;

        if (req.getMethod().equals("GET"))
            redirectUrl = "/log-in?redirectUrl=" + getEncodedFullURL(req);
        else
            redirectUrl = "/log-in";

        if (session == null || isSessionExpired(session))
            throw new ForbiddenException(redirectTo(redirectUrl));
    }
}
