package org.kadirov.servlet.exception;

import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Getter
public class HttpException extends RuntimeException {

    public static final String DEFAULT_REDIRECT_TO = "/";
    public static final String DEFAULT_FORWARD_TO = "/error";
    public static final int DEFAULT_STATUS_CODE = SC_INTERNAL_SERVER_ERROR;

    public final ExceptionNav Nav;
    public final int StatusCode;

    public HttpException(String message){
        super(message);
        StatusCode = DEFAULT_STATUS_CODE;
        Nav = ExceptionNav.forwardTo(DEFAULT_FORWARD_TO);
    }

    public HttpException(String message, int statusCode, Throwable cause){
        super(message, cause);
        StatusCode = statusCode;
        Nav = ExceptionNav.forwardTo(DEFAULT_FORWARD_TO);
    }

    public HttpException(String message, int statusCode){
        super(message);
        StatusCode = statusCode;
        Nav = ExceptionNav.forwardTo(DEFAULT_FORWARD_TO);
    }

    public HttpException(String message, int statusCode, ExceptionNav nav){
        super(message);
        StatusCode = statusCode;
        Nav = nav;
    }

    public HttpException(String message, int statusCode, ExceptionNav nav, Throwable cause){
        super(message, cause);
        StatusCode = statusCode;
        Nav = nav;
    }

    public HttpException(ExceptionNav exceptionNav) {
        Nav = exceptionNav;
        StatusCode = DEFAULT_STATUS_CODE;
    }
}
