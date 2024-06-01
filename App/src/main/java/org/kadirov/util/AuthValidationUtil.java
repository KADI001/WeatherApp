package org.kadirov.util;

import static java.util.Objects.isNull;

public final class AuthValidationUtil {

    private static final String LOGIN_VALIDATION_REGEXP = ".";
    private static final String PASSWORD_VALIDATION_REGEXP = ".";

    public static boolean isValidLogin(String login) {
        return !isNull(login) && !login.isEmpty() && login.matches(LOGIN_VALIDATION_REGEXP);
    }

    public static boolean isValidPassword(String password) {
        return !isNull(password) && !password.isEmpty() && password.matches(PASSWORD_VALIDATION_REGEXP);
    }
}
