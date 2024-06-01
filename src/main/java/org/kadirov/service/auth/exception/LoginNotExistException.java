package org.kadirov.service.auth.exception;

public class LoginNotExistException extends RuntimeException {
    public LoginNotExistException() {
    }

    public LoginNotExistException(String message) {
        super(message);
    }

    public LoginNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginNotExistException(Throwable cause) {
        super(cause);
    }

    public LoginNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
