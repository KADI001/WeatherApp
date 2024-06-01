package org.kadirov.servlet.exception;

public record ExceptionNav(
        NavType type,
        String url
) {

    public static ExceptionNav redirectTo(String url){
        return new ExceptionNav(NavType.REDIRECT_TO, url);
    }

    public static ExceptionNav forwardTo(String url){
        return new ExceptionNav(NavType.FORWARD_TO, url);
    }

    public enum NavType {
        REDIRECT_TO,
        FORWARD_TO
    }
}
