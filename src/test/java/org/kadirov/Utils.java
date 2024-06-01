package org.kadirov;

import java.util.UUID;

public final class Utils {

    private Utils() {
    }

    public static String genUniqueLogin(int uniquePostfixLength) {
        String postfix = UUID.randomUUID().toString();
        return "User_" + (uniquePostfixLength != -1 ? postfix.substring(0, uniquePostfixLength - 1) : postfix);
    }

    public static String genUniqueLogin() {
        return genUniqueLogin(-1);
    }

}
