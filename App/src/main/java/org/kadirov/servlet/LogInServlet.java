package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kadirov.service.auth.AuthService;
import org.kadirov.service.auth.exception.LoginNotExistException;
import org.kadirov.service.auth.exception.WrongPasswordException;
import org.kadirov.servlet.exception.InternalServerErrorException;
import org.kadirov.entity.session.Session;
import org.thymeleaf.context.WebContext;

import static org.kadirov.util.SessionUtils.refreshSessionCookie;
import static org.kadirov.util.ValidationUtil.validateLogin;
import static org.kadirov.util.ValidationUtil.validatePassword;

@Slf4j
@WebServlet(value = "/log-in", name = "LogIn")
public class LogInServlet extends BaseServlet {

    private AuthService authService;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        authService = (AuthService) servletContext.getAttribute("authService");
    }

    @Override
    protected String processGet(@NonNull final HttpServletRequest req,
                                @NonNull final HttpServletResponse resp,
                                @NonNull final WebContext webContext) {
        return "log-in";
    }

    @Override
    protected String processPost(@NonNull final HttpServletRequest req,
                                 @NonNull final HttpServletResponse resp,
                                 @NonNull final WebContext webContext) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String redirectUrl = req.getParameter("redirectUrl");

        if (!validateLogin(login)) {
            webContext.setVariable("loginInvalid", true);
            return "log-in";
        }

        if (!validatePassword(password)) {
            webContext.setVariable("passwordInvalid", true);
            return "log-in";
        }

        try {
            Session newSession = authService.logIn(login, password);

            refreshSessionCookie(req, resp, newSession.getId());

            if (redirectUrl == null || redirectUrl.isEmpty()) {
                resp.sendRedirect("/");
            } else {
                resp.sendRedirect(redirectUrl);
            }
        } catch (LoginNotExistException | WrongPasswordException e) {
            log.error("Failed to log in user.", e);
            webContext.setVariable("loginNotExistOrWrongPassword", true);
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error");
        }

        return "log-in";
    }
}
