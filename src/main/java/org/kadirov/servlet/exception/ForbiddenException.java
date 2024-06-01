package org.kadirov.servlet.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class ForbiddenException extends HttpException {

    public ForbiddenException() {
        super("");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, SC_FORBIDDEN, cause);
    }

    public ForbiddenException(String message, ExceptionNav nav) {
        super(message, SC_FORBIDDEN, nav);
    }

    public ForbiddenException(String message, ExceptionNav nav, Throwable cause) {
        super(message, SC_FORBIDDEN, nav, cause);
    }

    public ForbiddenException(ExceptionNav exceptionNav) {
        super(exceptionNav);
    }
}
