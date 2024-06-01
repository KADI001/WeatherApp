package org.kadirov.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kadirov.dao.session.SessionDAO;
import org.kadirov.util.Cookies;

import java.io.IOException;

import static org.kadirov.util.HttpRequestUtil.getCookieByName;

@SuppressWarnings("unused")
@WebFilter(urlPatterns = "/*")
public class SessionFetcherFilter implements Filter {

    public static final String SESSION_ATTRIBUTE_NAME = "session";
    public static final String NUMBER_PATTERN = "^[0-9]*$";
    public static final String RESOURCES_PATH = "/static";

    private SessionDAO sessionDAO;

    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        sessionDAO = (SessionDAO) servletContext.getAttribute("sessionDAO");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestURL = req.getRequestURI().substring(req.getServletContext().getContextPath().length());

        if (!requestURL.startsWith(RESOURCES_PATH)) {
            HttpSession httpSession = req.getSession();

            getCookieByName(req, Cookies.SESSION)
                    .flatMap(cookie -> sessionDAO.findById(cookie.getValue()))
                    .ifPresentOrElse(
                            session -> httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, session),
                            () -> httpSession.removeAttribute(SESSION_ATTRIBUTE_NAME)
                    );
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
