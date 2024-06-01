package org.kadirov.dao.user.exception;

public class LoginTooLongException extends RuntimeException {
    public LoginTooLongException() {
    }

    public LoginTooLongException(String message) {
        super(message);
    }

    public LoginTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginTooLongException(Throwable cause) {
        super(cause);
    }

    public LoginTooLongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
