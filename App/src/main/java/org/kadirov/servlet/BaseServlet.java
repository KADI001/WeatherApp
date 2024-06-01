package org.kadirov.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.kadirov.dto.ErrorView;
import org.kadirov.servlet.exception.ExceptionNav;
import org.kadirov.servlet.exception.HttpException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

import static org.kadirov.util.HttpRequestUtil.forwardToErrorPage;

@Slf4j
public abstract class BaseServlet extends HttpServlet implements PreService, PostServletInit {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication webApplication;

    @Override
    public final void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();

        templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");
        webApplication = (JakartaServletWebApplication) servletContext.getAttribute("webApplication");

        onInit(config);
    }

    @Override
    public final void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            onPreService(req, resp);
            super.service(req, resp);
        } catch (HttpException de) {
            log.error(String.format("Error occurred during execute '%s' method.", req.getMethod()), de.getCause());

            if (de.Nav.type() == ExceptionNav.NavType.REDIRECT_TO) {
                resp.setStatus(de.StatusCode);
                resp.sendRedirect(de.Nav.url());
            } else if (de.Nav.type() == ExceptionNav.NavType.FORWARD_TO) {
                forwardToErrorPage(req, resp, new ErrorView(de.StatusCode, de.getMessage()));
            }
        } catch (Exception e) {
            log.error(String.format("Error occurred during execute '%s' method.", req.getMethod()), e);
            forwardToErrorPage(req, resp, new ErrorView(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error"));
        }
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = getWebContext(req, resp);
        String path = processGet(req, resp, webContext);

        if (validatePath(path)) {
            templateEngine.process(path, webContext, resp.getWriter());
        }
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = getWebContext(req, resp);
        String path = processPost(req, resp, webContext);

        if (validatePath(path)) {
            templateEngine.process(path, webContext, resp.getWriter());
        }
    }

    @Override
    protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = getWebContext(req, resp);
        String path = processPut(req, resp, webContext);

        if (validatePath(path)) {
            templateEngine.process(path, webContext, resp.getWriter());
        }
    }

    @Override
    protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = getWebContext(req, resp);
        String path = processDelete(req, resp, webContext);

        if (validatePath(path)) {
            templateEngine.process(path, webContext, resp.getWriter());
        }
    }

    protected String processGet(@NonNull final HttpServletRequest req,
                                @NonNull final HttpServletResponse resp,
                                @NonNull final WebContext webContext) throws HttpException, ServletException, IOException {
        super.doGet(req, resp);
        return null;
    }

    protected String processPost(@NonNull final HttpServletRequest req,
                                 @NonNull final HttpServletResponse resp,
                                 @NonNull final WebContext webContext) throws HttpException, ServletException, IOException {
        super.doPost(req, resp);
        return null;
    }

    protected String processPut(@NonNull final HttpServletRequest req,
                                @NonNull final HttpServletResponse resp,
                                @NonNull final WebContext webContext) throws HttpException, ServletException, IOException {
        super.doPut(req, resp);
        return null;
    }

    protected String processDelete(@NonNull final HttpServletRequest req,
                                   @NonNull final HttpServletResponse resp,
                                   @NonNull final WebContext webContext) throws HttpException, ServletException, IOException {
        super.doDelete(req, resp);
        return null;
    }

    private static boolean validatePath(String path) {
        return path != null && !path.isEmpty();
    }

    private WebContext getWebContext(HttpServletRequest req, HttpServletResponse resp) {
        return new WebContext(webApplication.buildExchange(req, resp));
    }

}
