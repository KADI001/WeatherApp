package org.kadirov.util;

import org.kadirov.entity.session.Session;

import java.sql.Timestamp;
import java.time.Instant;

public final class ValidationUtil {

    public static final int MIN_LOGIN_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 5;

    private ValidationUtil() {
    }

    public static boolean validateStringParameter(String parameter, String regex) {
        return parameter != null && !parameter.isBlank() && parameter.matches(regex);
    }

    public static boolean validateStringParameter(String parameter) {
        return parameter != null && !parameter.isBlank();
    }

    public static boolean validateLogin(String login) {
        return validateStringParameter(login) && login.length() > MIN_LOGIN_LENGTH;
    }

    public static boolean validatePassword(String password) {
        return validateStringParameter(password) && password.length() > MIN_PASSWORD_LENGTH;
    }

    public static boolean isSessionExpired(Session session) {
        return session.getExpiresAt().getTime() < Timestamp.from(Instant.now()).getTime();
    }
}
