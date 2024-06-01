package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.entity.session.Session;
import org.kadirov.util.Cookies;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

import static org.kadirov.util.HttpRequestUtil.deleteCookieByName;

@WebServlet(value = "/log-out", name = "LogOut")
public class LogOutServlet extends AuthenticatedServlet {

    private SessionDAO sessionDAO;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
    }

    @Override
    protected String processGet(final @NonNull HttpServletRequest req,
                                final @NonNull HttpServletResponse resp,
                                final @NonNull WebContext webContext) throws IOException {
        HttpSession httpSession = req.getSession();
        Session session = (Session) httpSession.getAttribute("session");

        deleteCookieByName(req, resp, Cookies.SESSION);
        sessionDAO.deleteById(session.getId());

        resp.sendRedirect("/log-in");
        return null;
    }
}
