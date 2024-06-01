package org.kadirov.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.kadirov.dto.ErrorView;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(value = "/error",name = "Error")
public class ErrorServlet extends BaseServlet {

    @Override
    protected String processGet(@NonNull HttpServletRequest req, @NonNull HttpServletResponse resp, @NonNull WebContext webContext) throws RuntimeException, ServletException, IOException {
        webContext.setVariable("error", req.getAttribute("error"));
        return "error";
    }

    @Override
    protected String processPost(@NonNull final HttpServletRequest req,
                                 @NonNull final HttpServletResponse resp,
                                 @NonNull final WebContext webContext) {
        ErrorView error = (ErrorView) req.getAttribute("error");
        resp.setStatus(error.statusCode());

        webContext.setVariable("error", error);
        return "error";
    }
}
