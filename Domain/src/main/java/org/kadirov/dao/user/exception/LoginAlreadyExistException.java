package org.kadirov.dao.user.exception;

public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException() {
    }

    public LoginAlreadyExistException(String message) {
        super(message);
    }

    public LoginAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public LoginAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
