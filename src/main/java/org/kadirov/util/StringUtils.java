package org.kadirov.util;

import lombok.NonNull;

public final class StringUtils {

    private StringUtils() {}

    public static String limitText(@NonNull String text, int maxLength){
        return text.length() > maxLength ? text.substring(0, maxLength - 1) + "..." : text;
    }
}
