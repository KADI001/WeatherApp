package org.kadirov.servlet.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, SC_BAD_REQUEST, cause);
    }

    public BadRequestException(String message, ExceptionNav nav) {
        super(message, SC_BAD_REQUEST, nav);
    }

    public BadRequestException(String message, ExceptionNav nav, Throwable cause) {
        super(message, SC_BAD_REQUEST, nav, cause);
    }

    public BadRequestException(ExceptionNav exceptionNav) {
        super(exceptionNav);
    }
}
