package org.kadirov.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kadirov.service.auth.AuthService;
import org.kadirov.servlet.exception.InternalServerErrorException;
import org.kadirov.entity.session.Session;
import org.kadirov.dao.user.exception.LoginAlreadyExistException;
import org.kadirov.util.ValidationUtil;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

import static org.kadirov.util.SessionUtils.refreshSessionCookie;

@Slf4j
@WebServlet(value = "/sign-up", name = "SignUp")
public class SignUpServlet extends BaseServlet {

    private AuthService authService;

    @Override
    public void onInit(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        authService = (AuthService) servletContext.getAttribute("authService");
    }

    @Override
    protected String processGet(final @NonNull HttpServletRequest req,
                                final @NonNull HttpServletResponse resp,
                                final @NonNull WebContext webContext) {
        return "sign-up";
    }

    @Override
    protected String processPost(final @NonNull HttpServletRequest req,
                                 final @NonNull HttpServletResponse resp,
                                 final @NonNull WebContext webContext) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (!ValidationUtil.validateLogin(login)) {
            webContext.setVariable("loginInvalid", true);
            return "sign-up";
        }

        if (!ValidationUtil.validatePassword(password)) {
            webContext.setVariable("passwordInvalid", true);
            return "sign-up";
        }

        try {
            Session newSession = authService.signUp(login, password);

            refreshSessionCookie(req, resp, newSession.getId());
        } catch (LoginAlreadyExistException laee) {
            log.error("Error occurred during SignUpServlet.", laee);
            webContext.setVariable("loginExists", true);
            return "sign-up";
        } catch (Exception e) {
            throw new InternalServerErrorException("Internal Server Error");
        }

        resp.sendRedirect("/");
        return null;
    }
}
