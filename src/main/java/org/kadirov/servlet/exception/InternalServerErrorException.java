package org.kadirov.servlet.exception;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, SC_INTERNAL_SERVER_ERROR, cause);
    }

    public InternalServerErrorException(String message, ExceptionNav nav) {
        super(message, SC_INTERNAL_SERVER_ERROR, nav);
    }

    public InternalServerErrorException(String message, ExceptionNav nav, Throwable cause) {
        super(message, SC_INTERNAL_SERVER_ERROR, nav, cause);
    }
}
