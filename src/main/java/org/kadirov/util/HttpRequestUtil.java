package org.kadirov.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kadirov.dto.ErrorView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public final class HttpRequestUtil {
    private HttpRequestUtil(){}

    public static Optional<Cookie> getCookieByName(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();

        if(cookies == null || cookies.length == 0)
            return Optional.empty();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();
    }

    public static boolean deleteCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Optional<Cookie> optionalCookie = getCookieByName(request, cookieName);
        optionalCookie.ifPresent(cookie -> {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
        return optionalCookie.isPresent();
    }

    public static void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, ErrorView errorView) throws ServletException, IOException {
        request.setAttribute("error", errorView);
        request.getRequestDispatcher("/error").forward(request, response);
    }

    public static String getEncodedFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(encode(queryString)).toString();
        }
    }

    public static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
